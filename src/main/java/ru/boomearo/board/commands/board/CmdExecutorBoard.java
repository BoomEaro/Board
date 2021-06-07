package ru.boomearo.board.commands.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ru.boomearo.board.commands.AbstractExecutor;
import ru.boomearo.board.commands.CmdList;
import ru.boomearo.board.managers.BoardManager;

public class CmdExecutorBoard extends AbstractExecutor {

	public CmdExecutorBoard() {
		super(new CmdBoard());
	}

	@Override
	public boolean zeroArgument(CommandSender sender, CmdList cmds) {
		cmds.sendUsageCmds(sender);
		return true;
	}

	private static final List<String> empty = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			List<String> matches = new ArrayList<>();
			String search = arg3[0].toLowerCase();
			for (String se : Arrays.asList("scroll", "toggle", "page", "debug"))
			{
				if (se.toLowerCase().startsWith(search))
				{
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

