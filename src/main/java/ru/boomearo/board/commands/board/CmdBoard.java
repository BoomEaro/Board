package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.boomearo.board.Board;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.serverutils.utils.other.commands.CmdInfo;
import ru.boomearo.serverutils.utils.other.commands.Commands;

public class CmdBoard implements Commands {

    @CmdInfo(name = "toggle", description = "Переключить отображение.", usage = "/board toggle", permission = "")
    public boolean toggle(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        BoardManager manager = Board.getInstance().getBoardManager();
        PlayerBoard pb = manager.getPlayerBoard(pl.getName());
        PlayerToggle pt = manager.getOrCreatePlayerToggle(pl.getName());
        if (pb != null) {
            manager.removePlayerBoard(pl.getName());
            pt.setToggle(false);
            pl.sendMessage(BoardManager.prefix + "Вы успешно §cвыключили §fотображение.");
            return true;
        }

        manager.addPlayerBoard(new PlayerBoard(pl));
        pt.setToggle(true);
        pl.sendMessage(BoardManager.prefix + "Вы успешно §aвключили §fотображение.");
        return true;
    }

    @CmdInfo(name = "page", description = "Показать указанную страницу.", usage = "/board page <страница>", permission = "")
    public boolean page(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        Integer page = null;
        try {
            page = Integer.parseInt(args[0]);
        }
        catch (Exception ignored) {
        }
        if (page == null) {
            pl.sendMessage(BoardManager.prefix + "Аргумент должен содержать цифры!");
            return true;
        }

        BoardManager manager = Board.getInstance().getBoardManager();
        PlayerBoard pb = manager.getPlayerBoard(pl.getName());
        if (pb != null) {
            int maxSize = pb.getMaxPageIndex();
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

            if (pageTo.isVisibleToPlayer()) {
                pb.toPage(fixedPage, pageTo);
                pl.sendMessage(BoardManager.prefix + "Страница §6" + page + " §fуспешно отображена.");
            }
            else {
                pl.sendMessage(BoardManager.prefix + "Данная страница либо пуста либо у вас нет прав на ее отображение.");
            }
        }
        else {
            pl.sendMessage(BoardManager.prefix + "У вас выключено табло. Включите его командой §6/board toggle");
        }
        return true;
    }

    @CmdInfo(name = "scroll", description = "Переключить автоматические прокрутывание страниц. ", usage = "/board scroll", permission = "")
    public boolean permanent(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        BoardManager manager = Board.getInstance().getBoardManager();
        PlayerBoard pb = manager.getPlayerBoard(pl.getName());
        if (pb != null) {
            pb.setPermanentView(!pb.isPermanentView());
            pl.sendMessage(BoardManager.prefix + "Автоматическая прокрутка успешно " + (!pb.isPermanentView() ? "§aвключена" : "§cвыключена"));
        }
        else {
            pl.sendMessage(BoardManager.prefix + "У вас выключено табло. Включите его командой §6/board toggle");
        }
        return true;
    }

    @CmdInfo(name = "debug", description = "Переключить дебаг режим.", usage = "/board debug", permission = "")
    public boolean debug(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
            return true;
        }
        if (args.length != 0) {
            return false;
        }

        BoardManager manager = Board.getInstance().getBoardManager();
        PlayerBoard pb = manager.getPlayerBoard(pl.getName());
        if (pb != null) {
            pb.setDebugMode(!pb.isDebugMode());
            pl.sendMessage(BoardManager.prefix + "Дебаг режим " + (pb.isDebugMode() ? "§aвключен" : "§cвыключен"));
        }
        else {
            pl.sendMessage(BoardManager.prefix + "У вас выключено табло. Включите его командой §6/board toggle");
        }
        return true;
    }

    @CmdInfo(name = "reload", description = "Перезагрузить конфигурацию плагина.", usage = "/board reload", permission = "board.admin")
    public boolean reload(CommandSender cs, String[] args) {
        if (args.length != 0) {
            return false;
        }

        Board.getInstance().getBoardManager().loadConfig();

        cs.sendMessage(BoardManager.prefix + "Конфигурация успешно перезагружена!");
        return true;
    }

    @CmdInfo(name = "saveplayers", description = "Сохранить принудительно конфигурацию игроков.", usage = "/board saveplayers", permission = "board.admin")
    public boolean saveplayers(CommandSender cs, String[] args) {
        if (args.length != 0) {
            return false;
        }

        Board.getInstance().getBoardManager().savePlayersConfig();

        cs.sendMessage(BoardManager.prefix + "Конфигурация игроков успешно сохранена!");
        return true;
    }

}
