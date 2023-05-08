package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import ru.boomearo.board.managers.BoardManager;

import java.util.Arrays;
import java.util.List;

public class CommandReload extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandReload(CommandNodeBukkit root, BoardManager boardManager) {
        super(root, "reload", "board.command.reload");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§6/board reload §8-§a- Перезагрузить конфигурацию");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        this.boardManager.loadConfig();

        sender.sendMessage("§aКонфигурация успешно перезагружена!");
    }
}
