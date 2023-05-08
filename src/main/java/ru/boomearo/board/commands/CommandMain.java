package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandMain extends CommandNodeBukkit {

    public CommandMain() {
        super(null, "root", "board.command");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board §8-§a Эта команда");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sendHelp(sender);
    }
}
