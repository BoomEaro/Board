package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import ru.boomearo.board.Board;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Arrays;
import java.util.List;

public class CommandReload extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandReload(ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(configManager, root, "reload", "board.command.reload");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(this.configManager.getMessage("command_reload"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        this.configManager.load(Board.getInstance());
        this.boardManager.setPageListFactory(this.boardManager.getDefaultPageListFactory(), false);

        sender.sendMessage(this.configManager.getMessage("configuration_reloaded"));
    }
}
