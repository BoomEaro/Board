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
import ru.boomearo.board.objects.PageType;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.arcade.ArcadePageList;
import ru.boomearo.board.objects.boards.defaults.DefaultPageList;
import ru.boomearo.board.objects.boards.test.TestPageList;
import ru.boomearo.board.objects.hooks.HookManager;
import ru.boomearo.board.objects.hooks.TpsRunnable;
import ru.boomearo.board.runnable.BoardUpdater;

public class Board extends JavaPlugin {

    public static final long uptime = System.currentTimeMillis();

    private BoardManager boardManager = null;
    private HookManager hookManager = null;
    
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

        if (this.board == null) {
            this.board = new BoardUpdater();
            this.board.setPriority(3);
            this.board.start();
        }

        loadDefaultPageList();      
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


    public void loadDefaultPageList() {
        String m = getConfig().getString("mode");
        PageType tmpDpl = null;
        try {
            tmpDpl = PageType.valueOf(m);
        }
        catch (Exception e) {}
        if (tmpDpl == null) {
            tmpDpl = PageType.DefaultPage;
        }
        
        switch (tmpDpl) {
            case ArcadePage: {
                if (this.hookManager.getAdvEco() != null && this.hookManager.getCities() != null && this.hookManager.getMyPet() != null && this.hookManager.getNations() != null) {
                    
                    this.boardManager.setPageListFactory((PlayerBoard player) -> {
                        return new ArcadePageList(player);
                    });
                    
                    this.getLogger().info("Используем по умолчанию табло Аркадного сервера.");
                }
                else {
                    this.boardManager.setPageListFactory((PlayerBoard player) -> {
                        return new DefaultPageList(player);
                    });
                    
                    this.getLogger().info("Не хватает некоторых плагинов для использования табла Аркадного сервера. Используем пустое табло по умолчанию.");
                }
                break;
            }
            case DefaultPage: {
                this.boardManager.setPageListFactory((PlayerBoard player) -> {
                    return new DefaultPageList(player);
                });
                
                this.getLogger().info("Используем пустое табло по умолчанию.");
                break;
            }
            case TestPage: {
                this.boardManager.setPageListFactory((PlayerBoard player) -> {
                    return new TestPageList(player);
                });
                
                this.getLogger().info("Используем тестовое табло по умолчанию.");
                break;
            }
            default: {
                this.boardManager.setPageListFactory((PlayerBoard player) -> {
                    return new DefaultPageList(player);
                });
                break;
            }
        }
    }
    
    public static Board getInstance() { 
        return instance;
    }

    public HookManager getHookManager() {
        return this.hookManager;
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
