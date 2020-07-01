package ru.boomearo.board;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.board.commands.board.CmdExecutorBoard;
import ru.boomearo.board.listeners.PlayerListener;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.DefaultBoard;
import ru.boomearo.board.objects.boards.ServerBoard;
import ru.boomearo.board.objects.boards.TestBoard;
import ru.boomearo.board.objects.hooks.HookManager;
import ru.boomearo.board.objects.hooks.TpsRunnable;

public class Board extends JavaPlugin {

	public static final long uptime = System.currentTimeMillis();
	
	private int boardType = 1;
	
	private BoardManager boardManager = null;
	private HookManager hookManager = null;
	
	private TpsRunnable tps = null;
	
	public void onEnable() {
		instance = this;
		
	    File configFile = new File(getDataFolder() + File.separator + "config.yml");
	    if (!configFile.exists()) {
		    getLogger().info("Конфиг не найден, создаю новый...");
		    saveDefaultConfig();
		}
		
		if (this.hookManager == null) {
			this.hookManager = new HookManager();
		}
		
		if (this.boardManager == null) {
			this.boardManager = new BoardManager();
		}
		
		loadPlayersConfig(); 
		loadConfig();
		
		if (this.boardType == 1) {
			if (this.hookManager.getAdvEco() != null && this.hookManager.getCities() != null && this.hookManager.getMyPet() != null && this.hookManager.getNations() != null) {
				this.boardManager.setBoard(new ServerBoard());
				//this.boardManager.setBoard(new ExpBoard());
				this.getLogger().info("Загружаем аркадное табло.");
			}
			else {
				this.boardManager.setBoard(new DefaultBoard());
				this.getLogger().info("Не удалось загрузить аркадное табло. Кажется, отсутствует какой то плагин.");
			}
		}
		else if (this.boardType == 2) {
			this.boardManager.setBoard(new TestBoard());
			this.getLogger().info("Загружаем тестовое табло.");
		}
		else {
			this.boardManager.setBoard(new DefaultBoard());
			this.getLogger().info("Загружаем табло по умолчанию.");
		}
		
		getCommand("board").setExecutor(new CmdExecutorBoard());
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		loadPlayerBoards();
		
		getLogger().info("Плагин успешно загружен.");
	}
	
	public void onDisable() {
		unloadPlayerBoards();
		savePlayersConfig();
		getLogger().info("Плагин успешно выгружен.");
	}
	
	private static Board instance = null;
	public static Board getContext() { 
		if (instance != null) return instance; return null; 
	}
	
	public HookManager getHookManager() {
		return this.hookManager;
	}
	
	public BoardManager getBoardManager() {
		return this.boardManager;
	}
	
	public TpsRunnable getTpsRunnable() {
		
		if (this.tps == null) {
			this.tps = new TpsRunnable();
		}
		
		return this.tps;
	}
	
	public void loadPlayerBoards() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!this.boardManager.isIgnore(pl.getName())) {
				this.boardManager.addPlayerBoard(new PlayerBoard(pl));
			}
		}
	}
	public void unloadPlayerBoards() {
        for (PlayerBoard pb : this.boardManager.getAllPlayerBoards()) {
        	pb.remove();
        }
	}
	
	private void loadConfig() {
	    
	    this.boardType = this.getConfig().getInt("mode");
	    
	}
	
	
    private void loadPlayersConfig() {
        File playersConfigFile;
        FileConfiguration playersConfig;
        playersConfigFile = new File(getDataFolder(), "players.yml");
        if (!playersConfigFile.exists()) {
            this.getLogger().info("Конфигурация игроков не найдена, создаем новую..");
        	playersConfigFile.getParentFile().mkdirs();
            saveResource("players.yml", false);
         }

        playersConfig = new YamlConfiguration();
        try {
        	playersConfig.load(playersConfigFile);
        	
        	for (String pl : playersConfig.getStringList("ignores")) {
        		this.boardManager.addIgnore(pl);
        	}
            this.getLogger().info("Конфигурация игроков успешно загружена!");
        	
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void savePlayersConfig() {
        File playersConfigFile = new File(getDataFolder(), "players.yml");
        FileConfiguration playersConfig = new YamlConfiguration();

        playersConfig = new YamlConfiguration();
        
        playersConfig.set("ignores", this.boardManager.getAllIgnores());
        
        try {
        	playersConfig.save(playersConfigFile);
            this.getLogger().info("Конфигурация игроков успешно сохранена!");
        	
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
}
