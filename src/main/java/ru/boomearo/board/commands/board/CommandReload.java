package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.Board;
import ru.boomearo.board.commands.CommandNodeBukkit;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Collections;
import java.util.List;

public class CommandReload extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandReload(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(plugin, configManager, root, "reload", "board.command.reload");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(this.configManager.getMessage("command_reload"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        this.configManager.load(this.plugin);
        this.boardManager.setPageListFactory(this.boardManager.getPageListFactory());
        this.boardManager.unloadExecutor();
        this.boardManager.loadExecutor();
        this.boardManager.unloadPlayerBoards();
        this.boardManager.loadPlayerBoards();

        sender.sendMessage(this.configManager.getMessage("configuration_reloaded"));
    }
}
