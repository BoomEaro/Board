package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Arrays;
import java.util.List;

public class CommandMain extends CommandNodeBukkit {

    public CommandMain(ConfigManager configManager) {
        super(configManager, null, "root", "board.command");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(this.configManager.getMessage("command_main"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sendHelp(sender);
    }
}
