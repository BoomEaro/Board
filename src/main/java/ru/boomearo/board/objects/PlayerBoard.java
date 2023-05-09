package ru.boomearo.board.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import ru.boomearo.board.Board;
import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.AbstractHolder;

public class PlayerBoard {

    private final UUID uuid;
    private final Player player;
    private final BoardManager boardManager;

    private Scoreboard scoreboard;
    private Objective objective;

    private AbstractPageList pagesList = null;
    private final List<TeamInfo> teams = new ArrayList<>();
    private volatile int pageIndex = 0;

    private volatile int updatePageCount = 0;
    private boolean permanentView = false;
    private boolean debugMode = false;

    private final Object lock = new Object();

    private static final String TEAM_PREFIX = "BoardT_";

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
        //Удаляем панель если была
        removeBoardIfExists();
        //Строим ее заново
        buildScoreboard();

        //Устанавливаем список страниц по умолчанию
        setNewPageList(this.boardManager.getPageListFactory().createPageList(this));
    }

    private void buildScoreboard() throws BoardException {
        if (!Bukkit.isPrimaryThread()) {
            throw new BoardException("Scoreboard должен быть создан в основном потоке!");
        }

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("Board", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player.setScoreboard(this.scoreboard);
    }

    public void setNewPageList(AbstractPageList pageList) throws BoardException {
        synchronized (this.lock) {
            this.pageIndex = 0;

            this.pagesList = pageList;

            this.pagesList.loadPages();

            toPage(0, getCurrentPage());
        }
    }

    private void removeBoardIfExists() {
        Objective ob = this.player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if (ob != null) {
            ob.unregister();
            this.player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    public AbstractPageList getPageList() {
        return this.pagesList;
    }

    public AbstractPage getCurrentPage() {
        synchronized (this.lock) {
            return this.pagesList.getPages().get(this.pageIndex);
        }
    }

    public AbstractPage getPageByIndex(int index) {
        if (index < 0) {
            return null;
        }

        synchronized (this.lock) {
            if (index > getMaxPageIndex()) {
                return null;
            }

            return this.pagesList.getPages().get(index);
        }
    }

    public int getMaxPageIndex() {
        synchronized (this.lock) {
            return this.pagesList.getPages().size() - 1;
        }
    }

    public int getNextPageNumber() {
        synchronized (this.lock) {
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
    }

    public int getUpdatePageCount() {
        return this.updatePageCount;
    }

    public void addUpdatePageCount(int time) {
        this.updatePageCount = this.updatePageCount + time;
    }

    public void setUpdatePageCount(int time) {
        this.updatePageCount = time;
    }

    private void setUpPage(AbstractPage page) {
        //Устанавливаем страницу и заполняем лист тимами плейсхолдерами
        int index = BoardManager.MAX_ENTRY_SIZE;

        this.objective.setDisplayName(page.getBoardTitle());

        for (AbstractHolder holder : page.getReadyHolders()) {
            Team team = this.scoreboard.registerNewTeam(TEAM_PREFIX + index);
            TeamInfo teamInfo = new TeamInfo(team, holder, index);
            this.teams.add(teamInfo);

            String sc = BoardManager.getColor(index);
            team.addEntry(sc);
            this.objective.getScore(sc).setScore(index);
            index--;
        }
    }

    public void toPage(int indexTo, AbstractPage toPage) {
        synchronized (this.lock) {
            this.updatePageCount = 0;
            this.pageIndex = indexTo;

            loadPage(toPage);

            update();
        }
    }

    public void update() {
        synchronized (this.lock) {
            //Обновляем инфу согласно кастомным холдерам
            for (TeamInfo teamInfo : this.teams) {
                teamInfo.update();
            }
        }
    }

    public void remove() {
        synchronized (this.lock) {
            if (Bukkit.isPrimaryThread()) {
                this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
            else {
                Bukkit.getScheduler().runTask(Board.getInstance(), () -> this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
            }
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
                //Очищаем только те тимы которые мы создали.
                if (t.getName().contains(TEAM_PREFIX)) {
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
                if (getUpdatePageCount() >= thisPage.getTimeToChangePage()) {

                    //Убеждаемся что текущая страница не является следующей
                    //Board.getInstance().getLogger().info(pb.getPlayer().getDisplayName() + " -> " + nextPageIndex + " " + pb.getUpdatePageCount() + " " + (thisPage.getTimeToChange() / this.updateTime) + " " + (pb.getUpdatePageCount() >= (thisPage.getTimeToChange() / this.updateTime)) + " " + (pb.getPageIndex() != nextPageIndex) + " " + !thisPage.isVisible() + " " + !pb.isPermanentView());
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
            addUpdatePageCount(1);
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
}