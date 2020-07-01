package ru.boomearo.board.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import ru.boomearo.board.Board;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractHolder;

public class PlayerBoard {

	private Player player;
	
	private Scoreboard scoreboard;
	private Objective objective;
    
    private List<AbstractPage> pages = null;
    private List<TeamInfo> teams = new ArrayList<TeamInfo>();
	private int pageIndex = 0;

    private volatile boolean cd = false;
	
	private int updatePageCount = 0;
	private boolean permanentView = false;
	private boolean debugMode = false;

	
	public PlayerBoard(Player player) {
		this.player = player;
		
		Objective ob = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		if (ob != null) {
            ob.unregister();
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
		//Инициализируем панель
        buildScoreboard();
		//Получаем новые экземплярпы страниц для игрока
        initPages();
		//Устанавливаем страницу
		setUpPage(getCurrentPage());
		
	}
	
	public void initPages() {
		this.pageIndex = 0;
		
		this.pages = Board.getContext().getBoardManager().getBoard().getPages(this);
	}
	
	public AbstractPage getCurrentPage() {
		return this.pages.get(this.pageIndex);
	}
	
	public List<AbstractPage> getPages() {
		return this.pages;
	}
	
	public AbstractPage getPageByIndex(int index) {
		
		if (index < 0) {
			return null;
		}
		
		if (index > getMaxPageIndex()) {
			return null;
		}
		
		return this.pages.get(index);
	}
	
	public int getMaxPageIndex() {
		return this.pages.size() - 1;
	}
	
    public int getNextPageNumber() {
		int maxPage = getMaxPageIndex();
		for (int i = this.pageIndex; i <= maxPage; i++) {
			int next = i + 1;
			if (next > maxPage) {
				return 0;
			}
			AbstractPage nextPage = this.pages.get(next);
			if (nextPage.isVisible()) {
				return next;
			}
		}
		return 0;
		
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

    private void buildScoreboard() {
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
            Team team = this.scoreboard.registerNewTeam("Team:" + index);
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
		this.updatePageCount = 0;
		this.pageIndex = indexTo;
		loadPage(toPage);
		
		update();
    }

    public void update() {
        if (this.cd) {
            return;
        }
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

    private void setPrefix(Team team, String text) {
        team.setPrefix(text);
    }

    private void setSuffix(Team team, String text) {
        team.setSuffix(text);
    }

    public void remove() {
        this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    private void loadPage(AbstractPage page) {
        this.cd = true;
        removeAll();

        setUpPage(page);

        this.cd = false;
    }

    private void removeAll() {
        
        Set<Team> teams = this.scoreboard.getTeams();
        if (teams != null) {
            for (Team t : teams) {
                t.unregister();
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


    public Scoreboard getBoard() {
        return this.scoreboard;
    }

    public Objective getScore() {
        return this.objective;
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