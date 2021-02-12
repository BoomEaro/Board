package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.boomearo.board.Board;
import ru.boomearo.board.commands.CmdInfo;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;

public class CmdBoard {

	@CmdInfo(name = "toggle", description = "Переключить отображение.", usage = "/board toggle", permission = "")
	public boolean toggle(CommandSender cs, String[] args) {
		if (!(cs instanceof Player)) {
			return true;
		}
		if (args.length < 0 || args.length > 0) {
			return false;
		}
		Player pl = (Player) cs;
		BoardManager manager = Board.getInstance().getBoardManager();
		if (!manager.isIgnore(pl.getName())) {
			manager.removePlayerBoard(pl.getName());
			manager.addIgnore(pl.getName());
			pl.sendMessage(BoardManager.prefix + "Вы успешно §cвыключили §fотображение.");
		} 
		else {
			manager.addPlayerBoard(new PlayerBoard(pl));
			manager.removeIgnore(pl.getName());
			pl.sendMessage(BoardManager.prefix + "Вы успешно §aвключили §fотображение.");
		}
    	return true;
	}
	
	@CmdInfo(name = "page", description = "Показать указанную страницу.", usage = "/board page <страница>", permission = "")
	public boolean page(CommandSender cs, String[] args) {
		if (!(cs instanceof Player)) {
			return true;
		}
		if (args.length < 1 || args.length > 1) {
			return false;
		}
		Player pl = (Player) cs;
		if (!args[0].matches("\\d+")) {
			String ss = BoardManager.prefix + "Аргумент должен содержать цифры!";
			pl.sendMessage(ss);
			return true;
		}
		BoardManager manager = Board.getInstance().getBoardManager();
		PlayerBoard pb = manager.getPlayerBoard(pl.getName());
		if (pb != null) {
			int maxSize = pb.getMaxPageIndex();
			int page = Integer.parseInt(args[0]);
			int fixedPage = page - 1;
			if (fixedPage > maxSize) {
				pl.sendMessage(BoardManager.prefix + "Страница §6" + page + " §fне найдена.");
				return true;
			}
			if (fixedPage < 0) {
				pl.sendMessage(BoardManager.prefix + "Страница §6" + page + " §fне может быть нулем или меньше нуля.");
				return true;
			}
			
			AbstractPage pageTo = pb.getPageByIndex(fixedPage);
			boolean isVisible = false;
			try {
			    isVisible = pageTo.isVisible();
			}
			catch (Exception e) {
			    e.printStackTrace();
			}
			
			if (isVisible) {
				pb.toPage(fixedPage, pageTo);
				pl.sendMessage(BoardManager.prefix + "Страница §6" + page + " §fуспешно отображена.");
			}
			else {
				pl.sendMessage(BoardManager.prefix + "Данная страница либо пуста либо у вас нет прав на ее отображение.");
			}
		}
		else {
			pl.sendMessage(BoardManager.prefix + "Кажется, у вас выключен board.");
		}
    	return true;
	}

	@CmdInfo(name = "scroll", description = "Переключить автоматические прокрутывание страниц. ", usage = "/board scroll", permission = "")
	public boolean permanent(CommandSender cs, String[] args) {
		if (!(cs instanceof Player)) {
			return true;
		}
		if (args.length < 0 || args.length > 0) {
			return false;
		}
		Player pl = (Player) cs;
		BoardManager manager = Board.getInstance().getBoardManager();
		PlayerBoard pb = manager.getPlayerBoard(pl.getName());
		if (pb != null) {
			pb.setPermanentView(!pb.isPermanentView());
			pl.sendMessage(BoardManager.prefix + "Автоматическая прокрутка успешно " + (!pb.isPermanentView() ? "§aвключена" : "§cвыключена"));
		}
		else {
			pl.sendMessage(BoardManager.prefix + "Кажется, у вас выключен board.");
		}
    	return true;
	}
	@CmdInfo(name = "debug", description = "Переключить дебаг режим.", usage = "/board debug", permission = "")
	public boolean debug(CommandSender cs, String[] args) {
		if (!(cs instanceof Player)) {
			return true;
		}
		if (args.length < 0 || args.length > 0) {
			return false;
		}
		Player pl = (Player) cs;
		
		BoardManager manager = Board.getInstance().getBoardManager();
		PlayerBoard pb = manager.getPlayerBoard(pl.getName());
		if (pb != null) {
			pb.setDebugMode(!pb.isDebugMode());
			pl.sendMessage(BoardManager.prefix + "Дебаг режим " + (pb.isDebugMode() ? "§aвключен" : "§cвыключен"));
		}
		else {
			pl.sendMessage(BoardManager.prefix + "Кажется, у вас выключен board.");
		}
    	return true;
	}
	
	
}
