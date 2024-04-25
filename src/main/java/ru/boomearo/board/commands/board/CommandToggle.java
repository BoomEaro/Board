package ru.boomearo.board.commands.board;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.commands.CommandNodeBukkit;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;

import java.util.Collections;
import java.util.List;

public class CommandToggle extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandToggle(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(plugin, configManager, root, "toggle", "board.command.toggle");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(this.configManager.getMessage("command_toggle"));
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

        if (!this.configManager.isEnabledToggle()) {
            pl.sendMessage(this.configManager.getMessage("can_not_toggle"));
            return;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getUniqueId());
        PlayerToggle pt = this.boardManager.getOrCreatePlayerToggle(pl);
        if (pb != null) {
            this.boardManager.removePlayerBoard(pl);
            pt.setToggle(false);
            pl.sendMessage(this.configManager.getMessage("successfully_toggled_off"));
            return;
        }

        this.boardManager.addPlayerBoard(pl);
        pt.setToggle(true);
        pl.sendMessage(this.configManager.getMessage("successfully_toggled_on"));
    }
}
