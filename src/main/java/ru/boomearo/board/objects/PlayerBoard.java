package ru.boomearo.board.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import ru.boomearo.board.Board;
import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.AbstractTitleHolder;
import ru.boomearo.board.objects.boards.AbstractValueHolder;
import ru.boomearo.board.objects.boards.ScoreSequence;

public class PlayerBoard {

    private final UUID uuid;
    private final Player player;
    private final BoardManager boardManager;

    private Scoreboard scoreboard;
    private Objective objective;

    private AbstractPageList pagesList = null;

    private AbstractTitleHolder titleHolder = null;
    private String currentTitleResult = null;
    private final List<TeamInfo> teams = new ArrayList<>();

    private int pageIndex = 0;

    private long pageCreateTime = 0;
    private boolean permanentView = false;
    private boolean debugMode = false;
    private boolean init = false;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public PlayerBoard(UUID uuid, Player player, BoardManager boardManager) {
        this.uuid = uuid;
        this.player = player;
        this.boardManager = boardManager;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BoardManager getBoardManager() {
        return this.boardManager;
    }

    public void init() throws BoardException {
        if (this.init) {
            return;
        }
        //Удаляем панель если была
        removeBoardIfExists();
        //Строим ее заново
        buildScoreboard();
        this.init = true;
    }

    private void buildScoreboard() throws BoardException {
        if (!Bukkit.isPrimaryThread()) {
            throw new BoardException("Scoreboard must be create in the main thread!");
        }

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("Board", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player.setScoreboard(this.scoreboard);
    }

    private void removeBoardIfExists() {
        Objective ob = this.player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if (ob != null) {
            ob.unregister();
            this.player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    public void setNewPageList(AbstractPageList pageList) throws BoardException {
        this.readWriteLock.writeLock().lock();
        try {
            this.pageIndex = 0;

            this.pagesList = pageList;

            this.pagesList.loadPages();

            toPage(0, getCurrentPage());
        }
        finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    public AbstractPageList getPageList() {
        return this.pagesList;
    }

    public AbstractPage getCurrentPage() {
        this.readWriteLock.readLock().lock();
        try {
            return this.pagesList.getPages().get(this.pageIndex);
        }
        finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public AbstractPage getPageByIndex(int index) {
        if (index < 0) {
            return null;
        }

        this.readWriteLock.readLock().lock();
        try {
            if (index > getMaxPageIndex()) {
                return null;
            }

            return this.pagesList.getPages().get(index);
        }
        finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public int getMaxPageIndex() {
        this.readWriteLock.readLock().lock();
        try {
            return this.pagesList.getPages().size() - 1;
        }
        finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public int getNextPageNumber() {
        this.readWriteLock.readLock().lock();
        try {
            int maxPage = getMaxPageIndex();
            for (int i = this.pageIndex; i <= maxPage; i++) {
                int next = i + 1;
                if (next > maxPage) {
                    return 0;
                }
                AbstractPage nextPage = this.pagesList.getPages().get(next);
                if (nextPage.isVisibleToPlayer()) {
                    return next;
                }
            }
            return 0;
        }
        finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    private void setUpPage(AbstractPage page) {
        int index = BoardManager.MAX_ENTRY_SIZE;
        ScoreSequence scoreSequence = page.getScoreSequence().create();

        this.titleHolder = page.getReadyTitleHolder();

        applyTitleResult(this.titleHolder.getHolderResult());

        for (AbstractValueHolder holder : page.getReadyHolders()) {
            int scoreIndex = scoreSequence.getCurrentScore();
            Team team = this.scoreboard.registerNewTeam(BoardManager.TEAM_PREFIX + index);
            String entryNameColor = BoardManager.getColor(index);
            TeamInfo teamInfo = new TeamInfo(this.scoreboard, this.objective, team, holder, entryNameColor, scoreIndex);
            this.teams.add(teamInfo);

            scoreSequence.next();
            index--;

            teamInfo.update();
        }
    }

    public void toPage(int indexTo, AbstractPage toPage) {
        this.readWriteLock.writeLock().lock();
        try {
            this.pageCreateTime = System.currentTimeMillis();
            this.pageIndex = indexTo;

            loadPage(toPage);
            update();
        }
        finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    public void update() {
        this.readWriteLock.writeLock().lock();
        try {
            // Update title holder
            String newResult = this.titleHolder.getHolderResult();
            if (!newResult.equals(this.currentTitleResult)) {
                applyTitleResult(newResult);
            }

            // Update teams holders
            for (TeamInfo teamInfo : this.teams) {
                teamInfo.update();
            }
        }
        finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    private void applyTitleResult(String text) {
        this.currentTitleResult = text;
        this.objective.setDisplayName(text);
    }

    public void remove() {
        this.readWriteLock.writeLock().lock();
        try {
            if (Bukkit.isPrimaryThread()) {
                this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                return;
            }
            Bukkit.getScheduler().runTask(Board.getInstance(), () -> this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
        }
        finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    private void loadPage(AbstractPage page) {
        removeAll();
        setUpPage(page);
    }

    private void removeAll() {
        Set<Team> teams = this.scoreboard.getTeams();
        if (teams != null) {
            for (Team t : teams) {
                if (t.getName().contains(BoardManager.TEAM_PREFIX)) {
                    t.unregister();
                }
            }
        }

        Set<String> entry = this.scoreboard.getEntries();
        if (entry != null) {
            for (String s : entry) {
                this.scoreboard.resetScores(s);
            }
        }

        this.teams.clear();
    }

    public void handleBoard() {
        try {
            if (!this.init) {
                return;
            }

            int maxPage = getMaxPageIndex();
            if (getPageIndex() <= maxPage) {
                AbstractPage thisPage = getCurrentPage();

                int nextPageIndex = getNextPageNumber();
                AbstractPage nextPage = getPageByIndex(nextPageIndex);

                //Если текущая страница не видна игроку
                if (!thisPage.isVisibleToPlayer()) {

                    //Убеждаемся что текущая страница не является следующей страницей (в противном случае ничего не делаем)
                    if (getPageIndex() != nextPageIndex) {
                        toPage(nextPageIndex, nextPage);
                    }

                    return;
                }

                //Сменяем страницу только если прошло время, иначе просто обновляем ее
                if ((System.currentTimeMillis() - this.pageCreateTime) > thisPage.getTimeToChangePage()) {

                    //Убеждаемся что текущая страница не является следующей
                    if (getPageIndex() != nextPageIndex) {
                        //Если оказывается что в настройках игрока отключен авто скролл, то просто обновляем страницу.
                        //Иначе пытаемся открыть следующую страницу.
                        if (isPermanentView()) {
                            update();
                            return;
                        }

                        toPage(nextPageIndex, nextPage);

                        update();
                        return;
                    }
                }
                update();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int index) {
        this.pageIndex = index;
    }

    public void setPermanentView(boolean view) {
        this.permanentView = view;
    }

    public boolean isPermanentView() {
        return this.permanentView;
    }

    public void setDebugMode(boolean mode) {
        this.debugMode = mode;
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }

    public boolean isInit() {
        return this.init;
    }
}