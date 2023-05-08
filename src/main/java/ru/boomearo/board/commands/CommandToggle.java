package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;

import java.util.Arrays;
import java.util.List;

public class CommandToggle extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandToggle(CommandNodeBukkit root, BoardManager boardManager) {
        super(root, "toggle", "board.command.toggle");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board toggle §8-§a Переключить отображение.");
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

        if (!this.boardManager.isEnabledToggle()) {
            pl.sendMessage(BoardManager.PREFIX + "Вы не можете переключить отображение.");
            return;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        PlayerToggle pt = this.boardManager.getOrCreatePlayerToggle(pl.getName());
        if (pb != null) {
            this.boardManager.removePlayerBoard(pl.getName());
            pt.setToggle(false);
            pl.sendMessage(BoardManager.PREFIX + "Вы успешно §cвыключили §fотображение.");
            return;
        }

        this.boardManager.addPlayerBoard(new PlayerBoard(pl));
        pt.setToggle(true);
        pl.sendMessage(BoardManager.PREFIX + "Вы успешно §aвключили §fотображение.");
        return;
    }
}
