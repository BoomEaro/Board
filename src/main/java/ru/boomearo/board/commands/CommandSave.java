package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;

import ru.boomearo.board.managers.BoardManager;

import java.util.Arrays;
import java.util.List;

public class CommandSave extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandSave(CommandNodeBukkit root, BoardManager boardManager) {
        super(root, "save", "board.command.save");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board save §8-§a Сохранить принудительно конфигурацию игроков.");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        this.boardManager.savePlayersConfig();

        sender.sendMessage(BoardManager.PREFIX + "Конфигурация игроков успешно сохранена!");
        return;
    }
}
