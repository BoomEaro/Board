package ru.boomearo.board.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.boards.*;
import ru.boomearo.board.tasks.UsedExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Setter
@Getter
public class PlayerBoardImpl implements PlayerBoard {

    private final UUID uuid;
    private final Player player;
    private final Plugin plugin;
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

    private ScheduledFuture<?> scheduledFuture = null;
    private UsedExecutor usedExecutor = null;

    public PlayerBoardImpl(UUID uuid, Player player, Plugin plugin, BoardManager boardManager) {
        this.uuid = uuid;
        this.player = player;
        this.plugin = plugin;
        this.boardManager = boardManager;
    }

    public void bindUsedExecutor(int update, UsedExecutor usedExecutor) {
        if (this.usedExecutor != null) {
            throw new IllegalArgumentException("UsedExecutor is already bind!");
        }

        this.usedExecutor = usedExecutor;
        this.usedExecutor.takeExecutor();

        this.scheduledFuture = this.usedExecutor.schedule(this::handleBoard, update, update, TimeUnit.MILLISECONDS);
    }

    public void init() {
        if (this.init) {
            return;
        }
        // Удаляем панель если была
        removeBoardIfExists();
        // Строим ее заново
        buildScoreboard();
    }

    private void removeBoardIfExists() {
        Objective ob = this.player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if (ob != null) {
            ob.unregister();
            this.player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    private void buildScoreboard() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalArgumentException("Scoreboard must be create in the main thread!");
        }

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("Board", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player.setScoreboard(this.scoreboard);
    }

    @Override
    public void setNewPageList(AbstractPageList pageList) {
        if (this.usedExecutor == null) {
            throw new IllegalArgumentException("UsedExecutor is not bind!");
        }

        this.usedExecutor.execute(() -> {
            try {
                this.pageIndex = 0;

                this.pagesList = pageList;

                this.pagesList.loadPages();

                toPage0(0, getCurrentPage0());

                this.init = true;
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to set new page for player " + this.player.getName(), e);
            }
        });
    }

    @Override
    public CompletableFuture<AbstractPage> getCurrentPage() {
        if (this.usedExecutor == null) {
            throw new IllegalArgumentException("UsedExecutor is not bind!");
        }

        return this.usedExecutor.submit(this::getCurrentPage0);
    }

    private AbstractPage getCurrentPage0() {
        return this.pagesList.getPages().get(this.pageIndex);
    }

    @Override
    public CompletableFuture<AbstractPage> getPageByIndex(int index) {
        if (this.usedExecutor == null) {
            throw new IllegalArgumentException("UsedExecutor is not bind!");
        }

        return this.usedExecutor.submit(() -> getPageByIndex0(index));
    }

    private AbstractPage getPageByIndex0(int index) {
        if (index < 0) {
            return null;
        }

        if (index > getMaxPageIndex0()) {
            return null;
        }

        return this.pagesList.getPages().get(index);
    }

    @Override
    public CompletableFuture<Integer> getMaxPageIndex() {
        if (this.usedExecutor == null) {
            throw new IllegalArgumentException("UsedExecutor is not bind!");
        }

        return this.usedExecutor.submit(this::getMaxPageIndex0);
    }

    private int getMaxPageIndex0() {
        return this.pagesList.getPages().size() - 1;
    }

    @Override
    public CompletableFuture<Integer> getNextPageNumber() {
        if (this.usedExecutor == null) {
            throw new IllegalArgumentException("UsedExecutor is not bind!");
        }

        return this.usedExecutor.submit(this::getNextPageNumber0);
    }

    private int getNextPageNumber0() {
        int maxPage = getMaxPageIndex0();
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

    @Override
    public void toPage(int indexTo, AbstractPage toPage) {
        if (this.usedExecutor == null) {
            throw new IllegalArgumentException("UsedExecutor is not bind!");
        }

        this.usedExecutor.execute(() -> toPage0(indexTo, toPage));
    }

    private void toPage0(int indexTo, AbstractPage toPage) {
        this.pageCreateTime = System.currentTimeMillis();
        this.pageIndex = indexTo;

        loadPage(toPage);
        update();
    }

    @Override
    public void submitUpdate() {
        if (this.usedExecutor == null) {
            return;
        }

        this.usedExecutor.execute(this::handleBoard);
    }

    private void update() {
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

    private void applyTitleResult(String text) {
        this.currentTitleResult = text;
        this.objective.setDisplayName(text);
    }

    @Override
    public void remove() {
        this.init = false;

        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(false);
            this.scheduledFuture = null;
        }

        if (this.usedExecutor != null) {
            this.usedExecutor.restoreExecutor();
            this.usedExecutor = null;
        }

        if (Bukkit.isPrimaryThread()) {
            cancelScoreboard();
            return;
        }
        Bukkit.getScheduler().runTask(this.plugin, this::cancelScoreboard);
    }

    private void cancelScoreboard() {
        this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
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

    private void handleBoard() {
        if (!this.init) {
            return;
        }

        try {
            int maxPage = getMaxPageIndex0();
            if (this.pageIndex <= maxPage) {
                AbstractPage thisPage = getCurrentPage0();

                thisPage.performUpdate();

                int nextPageIndex = getNextPageNumber0();
                AbstractPage nextPage = getPageByIndex0(nextPageIndex);

                // Если текущая страница не видна игроку
                if (!thisPage.isVisibleToPlayer()) {

                    // Убеждаемся что текущая страница не является следующей страницей (в противном случае ничего не делаем)
                    if (this.pageIndex != nextPageIndex) {
                        toPage0(nextPageIndex, nextPage);
                    }

                    return;
                }

                // Сменяем страницу только если прошло время, иначе просто обновляем ее
                if ((System.currentTimeMillis() - this.pageCreateTime) > thisPage.getTimeToChangePage()) {

                    // Убеждаемся что текущая страница не является следующей
                    if (this.pageIndex != nextPageIndex) {
                        // Если оказывается что в настройках игрока отключен авто скролл, то просто обновляем страницу.
                        // Иначе пытаемся открыть следующую страницу.
                        if (this.permanentView) {
                            update();
                            return;
                        }

                        toPage0(nextPageIndex, nextPage);

                        update();
                        return;
                    }
                }
                update();
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to handle board for player " + this.player.getName(), e);
        }
    }
}