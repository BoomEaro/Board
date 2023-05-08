package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.serverutils.utils.other.commands.CmdInfo;
import ru.boomearo.serverutils.utils.other.commands.Commands;

public class CmdBoard implements Commands {

    private final BoardManager boardManager;

    public CmdBoard(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @CmdInfo(name = "toggle", description = "Переключить отображение.", usage = "/board toggle", permission = "")
    public boolean toggle(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        if (!this.boardManager.isEnabledToggle()) {
            pl.sendMessage(BoardManager.PREFIX + "Вы не можете переключить отображение.");
            return true;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        PlayerToggle pt = this.boardManager.getOrCreatePlayerToggle(pl.getName());
        if (pb != null) {
            this.boardManager.removePlayerBoard(pl.getName());
            pt.setToggle(false);
            pl.sendMessage(BoardManager.PREFIX + "Вы успешно §cвыключили §fотображение.");
            return true;
        }

        this.boardManager.addPlayerBoard(new PlayerBoard(pl));
        pt.setToggle(true);
        pl.sendMessage(BoardManager.PREFIX + "Вы успешно §aвключили §fотображение.");
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
            pl.sendMessage(BoardManager.PREFIX + "Аргумент должен содержать цифры!");
            return true;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        if (pb == null) {
            pl.sendMessage(BoardManager.PREFIX + "Сперва включите отображение командой §6/board toggle");
            return true;
        }

        int maxSize = pb.getMaxPageIndex();
        int fixedPage = page - 1;
        if (fixedPage > maxSize) {
            pl.sendMessage(BoardManager.PREFIX + "Страница §6" + page + " §fне найдена.");
            return true;
        }
        if (fixedPage < 0) {
            pl.sendMessage(BoardManager.PREFIX + "Страница §6" + page + " §fне может быть нулем или меньше нуля.");
            return true;
        }

        AbstractPage pageTo = pb.getPageByIndex(fixedPage);
        if (!pageTo.isVisibleToPlayer()) {
            pl.sendMessage(BoardManager.PREFIX + "Данная страница либо пуста либо у вас нет прав на ее отображение.");
            return true;
        }

        pb.toPage(fixedPage, pageTo);
        pl.sendMessage(BoardManager.PREFIX + "Страница §6" + page + " §fуспешно отображена.");
        return true;
    }

    @CmdInfo(name = "scroll", description = "Переключить автоматические прокручивание страниц. ", usage = "/board scroll", permission = "")
    public boolean permanent(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        if (pb == null) {
            pl.sendMessage(BoardManager.PREFIX + "Сперва включите отображение командой §6/board toggle");
            return true;
        }

        pb.setPermanentView(!pb.isPermanentView());
        pl.sendMessage(BoardManager.PREFIX + "Автоматическая прокрутка успешно " + (!pb.isPermanentView() ? "§aвключена" : "§cвыключена"));
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

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        if (pb == null) {
            pl.sendMessage(BoardManager.PREFIX + "Сперва включите отображение командой §6/board toggle");
            return true;
        }

        pb.setDebugMode(!pb.isDebugMode());
        pl.sendMessage(BoardManager.PREFIX + "Дебаг режим " + (pb.isDebugMode() ? "§aвключен" : "§cвыключен"));
        return true;
    }

    @CmdInfo(name = "reload", description = "Перезагрузить конфигурацию плагина.", usage = "/board reload", permission = "board.admin")
    public boolean reload(CommandSender cs, String[] args) {
        if (args.length != 0) {
            return false;
        }

        this.boardManager.loadConfig();

        cs.sendMessage(BoardManager.PREFIX + "Конфигурация успешно перезагружена!");
        return true;
    }

    @CmdInfo(name = "saveplayers", description = "Сохранить принудительно конфигурацию игроков.", usage = "/board saveplayers", permission = "board.admin")
    public boolean saveplayers(CommandSender cs, String[] args) {
        if (args.length != 0) {
            return false;
        }

        this.boardManager.savePlayersConfig();

        cs.sendMessage(BoardManager.PREFIX + "Конфигурация игроков успешно сохранена!");
        return true;
    }

}
