package ru.boomearo.board;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.board.commands.board.CmdExecutorBoard;
import ru.boomearo.board.listeners.PlayerListener;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.runnable.TpsRunnable;
import ru.boomearo.board.runnable.BoardUpdater;

public class Board extends JavaPlugin {

    private BoardManager boardManager = null;

    private BoardUpdater board = null;

    private TpsRunnable tps = null;

    private static Board instance = null;

    @Override
    public void onEnable() {
        instance = this;

        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Конфиг не найден, создаю новый...");
            saveDefaultConfig();
        }

        if (this.boardManager == null) {
            this.boardManager = new BoardManager();

            this.boardManager.loadConfig();
            this.boardManager.loadPlayersConfig();

            this.boardManager.loadPlayerBoards();
        }

        if (this.board == null) {
            this.board = new BoardUpdater();
            this.board.setPriority(3);
            this.board.start();
        }

        getCommand("board").setExecutor(new CmdExecutorBoard());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info("Плагин успешно загружен.");
    }

    @Override
    public void onDisable() {
        this.board.interrupt();

        if (this.boardManager != null) {
            this.boardManager.unloadPlayerBoards();
            this.boardManager.savePlayersConfig();
        }

        getLogger().info("Плагин успешно выгружен.");
    }


    public static Board getInstance() {
        return instance;
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

}
