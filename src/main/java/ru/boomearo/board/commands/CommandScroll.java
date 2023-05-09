package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Arrays;
import java.util.List;

public class CommandScroll extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandScroll(CommandNodeBukkit root, BoardManager boardManager) {
        super(root, "scroll", "board.command.scroll");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board scroll §8-§a Переключить автоматические прокручивание страниц.");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player pl)) {
            return;
        }

        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getUniqueId());
        if (pb == null) {
            pl.sendMessage(BoardManager.PREFIX + "Сперва включите отображение командой §6/board toggle");
            return;
        }

        pb.setPermanentView(!pb.isPermanentView());
        pl.sendMessage(BoardManager.PREFIX + "Автоматическая прокрутка успешно " + (!pb.isPermanentView() ? "§aвключена" : "§cвыключена"));
        return;
    }
}
