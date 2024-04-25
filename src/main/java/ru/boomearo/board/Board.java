package ru.boomearo.board;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.board.commands.board.CommandBoardExecutor;
import ru.boomearo.board.database.DatabaseRepository;
import ru.boomearo.board.hooks.PlaceHolderAPIHook;
import ru.boomearo.board.listeners.PlayerListener;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.utils.StringLength;

@Getter
public class Board extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseRepository databaseRepository;
    private BoardManager boardManager;

    @Getter
    private static Board instance = null;

    @Override
    public void onEnable() {
        instance = this;

        StringLength.init(this);

        Plugin placeHolderApiPlugin = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        PlaceHolderAPIHook placeHolderAPIHook;
        if (placeHolderApiPlugin != null) {
            placeHolderAPIHook = PlaceholderAPI::setPlaceholders;
            getLogger().info("Successfully hooked with PlaceHolderAPI!");
        } else {
            placeHolderAPIHook = (player, text) -> text;
        }

        this.configManager = new ConfigManager();
        this.configManager.load(this);

        this.databaseRepository = new DatabaseRepository(this);
        this.databaseRepository.load();

        this.boardManager = new BoardManager(this, this.configManager, this.databaseRepository, placeHolderAPIHook);
        this.boardManager.load();

        getCommand("board").setExecutor(new CommandBoardExecutor(this, this.configManager, this.boardManager));

        getServer().getPluginManager().registerEvents(new PlayerListener(this, this.boardManager), this);

        getLogger().info("Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {
        this.databaseRepository.unload();
        this.boardManager.unload();

        getLogger().info("Plugin successfully disabled.");
    }
}
