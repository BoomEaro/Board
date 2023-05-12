package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;

import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Arrays;
import java.util.List;

public class CommandSave extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandSave(ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(configManager, root, "save", "board.command.save");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(this.configManager.getMessage("command_save"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        this.boardManager.savePlayersConfig();

        sender.sendMessage(this.configManager.getMessage("successfully_saved"));
        return;
    }
}
