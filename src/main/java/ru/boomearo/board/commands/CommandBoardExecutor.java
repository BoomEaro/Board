package ru.boomearo.board.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.boomearo.board.managers.BoardManager;

import java.util.ArrayList;
import java.util.List;

public class CommandBoardExecutor implements CommandExecutor, TabCompleter {

    private final CommandNodeBukkit node;

    public CommandBoardExecutor(BoardManager boardManager) {
        CommandMain root = new CommandMain();
        root.addNode(new CommandToggle(root, boardManager));
        root.addNode(new CommandPage(root, boardManager));
        root.addNode(new CommandScroll(root, boardManager));
        root.addNode(new CommandDebug(root, boardManager));
        root.addNode(new CommandSave(root, boardManager));
        root.addNode(new CommandReload(root, boardManager));

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
