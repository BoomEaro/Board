package ru.boomearo.board.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class CommandBoardExecutor implements CommandExecutor, TabCompleter {

    private final CommandNodeBukkit node;

    public CommandBoardExecutor(ConfigManager configManager, BoardManager boardManager) {
        CommandMain root = new CommandMain(configManager);
        root.addNode(new CommandToggle(configManager, root, boardManager));
        root.addNode(new CommandPage(configManager, root, boardManager));
        root.addNode(new CommandScroll(configManager, root, boardManager));
        root.addNode(new CommandDebug(configManager, root, boardManager));
        root.addNode(new CommandSave(configManager, root, boardManager));
        root.addNode(new CommandReload(configManager, root, boardManager));

        this.node = root;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.node.execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>(this.node.suggest(sender, args));
    }

}
