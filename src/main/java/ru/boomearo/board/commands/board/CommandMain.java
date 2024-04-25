package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.commands.CommandNodeBukkit;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Collections;
import java.util.List;

public class CommandMain extends CommandNodeBukkit {

    public CommandMain(Plugin plugin, ConfigManager configManager) {
        super(plugin, configManager, null, "root", "board.command");
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(this.configManager.getMessage("command_main"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sendHelp(sender);
    }
}
