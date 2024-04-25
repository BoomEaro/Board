package ru.boomearo.board.commands.board;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.commands.CommandNodeBukkit;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class CommandBoardExecutor implements CommandExecutor, TabCompleter {

    private final CommandNodeBukkit node;

    public CommandBoardExecutor(Plugin plugin, ConfigManager configManager, BoardManager boardManager) {
        CommandMain root = new CommandMain(plugin, configManager);
        root.addNode(new CommandToggle(plugin, configManager, root, boardManager));
        root.addNode(new CommandPage(plugin, configManager, root, boardManager));
        root.addNode(new CommandScroll(plugin, configManager, root, boardManager));
        root.addNode(new CommandDebug(plugin, configManager, root, boardManager));
        root.addNode(new CommandReload(plugin, configManager, root, boardManager));

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
