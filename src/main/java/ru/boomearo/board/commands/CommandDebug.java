package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Arrays;
import java.util.List;

public class CommandDebug extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandDebug(ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(configManager, root, "debug", "board.command.debug");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(this.configManager.getMessage("command_debug"));
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
            pl.sendMessage(this.configManager.getMessage("board_should_be_enabled"));
            return;
        }

        pb.setDebugMode(!pb.isDebugMode());
        pl.sendMessage((pb.isDebugMode() ? this.configManager.getMessage("successfully_debug_on") : this.configManager.getMessage("successfully_debug_off")));
        return;
    }
}
