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
import ru.boomearo.board.runnable.TpsRunnable;
import ru.boomearo.board.runnable.BoardUpdater;

public class Board extends JavaPlugin {

    private BoardManager boardManager = null;
    
    private BoardUpdater board = null;
    
    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private int maxLenght = 16;

    private TpsRunnable tps = null;
    
    private static Board instance = null;

    public void onEnable() {
        instance = this;

        if (this.serverVersion.equalsIgnoreCase("1_12_R1")) {
            this.maxLenght = 16;
            getLogger().info("Старая версия сервера. Используем максимальную длину 16 символов.");
        }
        else {
            this.maxLenght = 64;
            getLogger().info("Новая версия сервера. Используем максимальную возможную длину символов.");
        }

        if (this.boardManager == null) {
            this.boardManager = new BoardManager();
        }

        if (this.board == null) {
            this.board = new BoardUpdater();
            this.board.setPriority(3);
            this.board.start();
        }

        loadPlayersConfig(); 
        loadPlayerBoards();

        getCommand("board").setExecutor(new CmdExecutorBoard());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info("Плагин успешно загружен.");
    }

    public void onDisable() {
        this.board.interrupt();

        unloadPlayerBoards();
        savePlayersConfig();
        
        getLogger().info("Плагин успешно выгружен.");
    }

    
    public static Board getInstance() { 
        return instance;
    }

    public BoardManager getBoardManager() {
        return this.boardManager;
    }

    public int getMaxLenght() {
        return this.maxLenght;
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
