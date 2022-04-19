package ru.boomearo.board.commands.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.serverutils.utils.other.commands.AbstractExecutor;

public class CmdExecutorBoard extends AbstractExecutor implements TabCompleter {

    private static final List<String> empty = new ArrayList<>();

    public CmdExecutorBoard(BoardManager boardManager) {
        super(new CmdBoard(boardManager));
    }

    @Override
    public boolean zeroArgument(CommandSender sender) {
        sendUsageCommands(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            String search = args[0].toLowerCase();
            List<String> tmp = new ArrayList<>(Arrays.asList("scroll", "toggle", "page", "debug"));
            if (sender.hasPermission("board.admin")) {
                tmp.add("reload");
                tmp.add("saveplayers");
            }
            for (String se : tmp) {
                if (se.toLowerCase().startsWith(search)) {
                    matches.add(se);
                }
            }
            return matches;
        }
        return empty;
    }

    @Override
    public String getPrefix() {
        return BoardManager.prefix;
    }

    @Override
    public String getSuffix() {
        return " §8-§a ";
    }


}

