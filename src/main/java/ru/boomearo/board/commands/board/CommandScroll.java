package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.commands.CommandNodeBukkit;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Collections;
import java.util.List;

public class CommandScroll extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandScroll(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(plugin, configManager, root, "scroll", "board.command.scroll");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(this.configManager.getMessage("command_scroll"));
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

        pb.setPermanentView(!pb.isPermanentView());
        pl.sendMessage((!pb.isPermanentView() ? this.configManager.getMessage("successfully_scroll_on") : this.configManager.getMessage("successfully_scroll_off")));
    }
}
