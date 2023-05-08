package ru.boomearo.board;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.board.commands.CommandBoardExecutor;
import ru.boomearo.board.listeners.PlayerListener;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.tasks.BoardUpdateTask;
import ru.boomearo.board.utils.StringLength;

public class Board extends JavaPlugin {

    private BoardManager boardManager = null;
    private BoardUpdateTask boardUpdateTask = null;

    private static Board instance = null;

    @Override
    public void onEnable() {
        instance = this;

        StringLength.init(this);

        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Конфиг не найден, создаю новый...");
            saveDefaultConfig();
        }

        this.boardManager = new BoardManager();
        this.boardManager.load();

        getCommand("board").setExecutor(new CommandBoardExecutor(this.boardManager));

        getServer().getPluginManager().registerEvents(new PlayerListener(this.boardManager), this);

        getLogger().info("Плагин успешно загружен.");
    }

    @Override
    public void onDisable() {
        this.boardManager.unload();

        getLogger().info("Плагин успешно выгружен.");
    }

    public static Board getInstance() {
        return instance;
    }

    public BoardManager getBoardManager() {
        return this.boardManager;
    }

    public BoardUpdateTask getBoardUpdater() {
        return this.boardUpdateTask;
    }

}
