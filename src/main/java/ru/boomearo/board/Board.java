package ru.boomearo.board;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.board.commands.CommandBoardExecutor;
import ru.boomearo.board.listeners.PlayerListener;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.utils.StringLength;

public class Board extends JavaPlugin {

    private ConfigManager configManager;
    private BoardManager boardManager;

    private static Board instance = null;

    @Override
    public void onEnable() {
        instance = this;

        StringLength.init(this);

        this.configManager = new ConfigManager();
        this.configManager.load(this);

        this.boardManager = new BoardManager(this.configManager);
        this.boardManager.load();

        getCommand("board").setExecutor(new CommandBoardExecutor(this.configManager, this.boardManager));

        getServer().getPluginManager().registerEvents(new PlayerListener(this.boardManager), this);

        getLogger().info("Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {
        this.boardManager.unload();

        getLogger().info("Plugin successfully disabled.");
    }

    public static Board getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public BoardManager getBoardManager() {
        return this.boardManager;
    }
}
