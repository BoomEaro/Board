package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;

import java.util.Arrays;
import java.util.List;

public class CommandPage extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandPage(CommandNodeBukkit root, BoardManager boardManager) {
        super(root, "page", "board.command.page");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board page <страница> §8-§a Показать указанную страницу.");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player pl)) {
            return;
        }

        if (args.length != 1) {
            sendCurrentHelp(sender);
            return;
        }

        Integer page;
        try {
            page = Integer.parseInt(args[0]);
        }
        catch (Exception ignored) {
            pl.sendMessage(BoardManager.PREFIX + "Аргумент должен содержать цифры!");
            return;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        if (pb == null) {
            pl.sendMessage(BoardManager.PREFIX + "Сперва включите отображение командой §6/board toggle");
            return;
        }

        int maxSize = pb.getMaxPageIndex();
        int fixedPage = page - 1;
        if (fixedPage > maxSize) {
            pl.sendMessage(BoardManager.PREFIX + "Страница §6" + page + " §fне найдена.");
            return;
        }
        if (fixedPage < 0) {
            pl.sendMessage(BoardManager.PREFIX + "Страница §6" + page + " §fне может быть нулем или меньше нуля.");
            return;
        }

        AbstractPage pageTo = pb.getPageByIndex(fixedPage);
        if (!pageTo.isVisibleToPlayer()) {
            pl.sendMessage(BoardManager.PREFIX + "Данная страница либо пуста либо у вас нет прав на ее отображение.");
            return;
        }

        pb.toPage(fixedPage, pageTo);
        pl.sendMessage(BoardManager.PREFIX + "Страница §6" + page + " §fуспешно отображена.");
        return;
    }
}
