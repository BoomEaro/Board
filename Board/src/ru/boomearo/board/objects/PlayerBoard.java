package ru.boomearo.board.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private final Player player;

    private Scoreboard scoreboard;
    private Objective objective;

    private AbstractPageList pagesList = null;
    private final List<TeamInfo> teams = new ArrayList<TeamInfo>();
    private volatile int pageIndex = 0;
    
    private volatile int updatePageCount = 0;
    private boolean permanentView = false;
    private boolean debugMode = false;

    private final Object lock = new Object();

    private static final String teamPrefix = "BoardT_";
    
    public PlayerBoard(Player player) {
        this.player = player;
        //Инициализируем панель
        try {
            //Удаляем панель если была
            removeBoardIfExists();
            //Строим ее заново
            buildScoreboard();
            
            //Устанавливаем список страниц по умолчанию
            setNewPageList(BoardManager.createDefaultPageList(Board.getInstance().getBoardManager().getDefaultPageList(), this));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private void removeBoardIfExists() {
        Objective ob = this.player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if (ob != null) {
            ob.unregister();
            this.player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    public void setNewPageList(AbstractPageList pageList) throws BoardException {      
        synchronized (this.lock) {  
            this.pagesList = pageList;
            
            this.pagesList.loadPages();
            
            toPage(0, getCurrentPage());
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
                if (nextPage.isVisible()) {
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

    private void buildScoreboard() throws BoardException {
        if (!Bukkit.isPrimaryThread()) {
            throw new BoardException("Scoreboard должен быть создан в основном потоке!");
        }

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("Board", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player.setScoreboard(this.scoreboard);
    }

    private void setUpPage(AbstractPage page) {
        //Устанавливаем страницу и заполняем лист тимами с плейсхолдерами
        int index = 15;

        this.objective.setDisplayName(page.getTitle());

        for (AbstractHolder holder : page.getReadyHolders()) {
            Team team = this.scoreboard.registerNewTeam(teamPrefix + index);
            String sc = BoardManager.getColor(index);
            team.addEntry(sc);
            String[] text = holder.getResult();
            setPrefix(team, text[0]);
            setSuffix(team, text[1]);
            this.objective.getScore(sc).setScore(index);
            this.teams.add(new TeamInfo(team, holder));
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
            for (TeamInfo team : this.teams) {
                //long start = System.nanoTime();
                String[] text = team.getHolder().getResult();
                //long end = System.nanoTime();
                //Board.getContext().getLogger().info("'" + text[0] + text[1] + "' -- " +(end - start));

                //Board.getContext().getLogger().info("test " + test[0] + "§r<->" + test[1]);
                setPrefix(team.getTeam(), text[0]);

                setSuffix(team.getTeam(), text[1]);

                //Board.getContext().getLogger().info("test " + test[0] + " " + test[1]);
            }   
        } 
    }

    private void setPrefix(Team team, String text) {
        team.setPrefix(text);
    }

    private void setSuffix(Team team, String text) {
        team.setSuffix(text);
    }

    public void remove() {
        synchronized (this.lock) {
            if (Bukkit.isPrimaryThread()) {
                this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
            else {
                Bukkit.getScheduler().runTask(Board.getInstance(), () -> {
                    this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                });
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
                if (t.getName().contains(teamPrefix)) {
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

    public Player getPlayer() {
        return this.player;
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