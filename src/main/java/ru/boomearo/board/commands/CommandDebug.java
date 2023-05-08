package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Arrays;
import java.util.List;

public class CommandDebug extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandDebug(CommandNodeBukkit root, BoardManager boardManager) {
        super(root, "debug", "board.command.debug");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board debug §8-§a Переключить дебаг режим.");
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

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        if (pb == null) {
            pl.sendMessage(BoardManager.PREFIX + "Сперва включите отображение командой §6/board toggle");
            return;
        }

        pb.setDebugMode(!pb.isDebugMode());
        pl.sendMessage(BoardManager.PREFIX + "Дебаг режим " + (pb.isDebugMode() ? "§aвключен" : "§cвыключен"));
        return;
    }
}
