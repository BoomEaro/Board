package ru.boomearo.board.commands.board;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.commands.CommandNodeBukkit;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerBoardData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        if (!(sender instanceof Player player)) {
            return;
        }

        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        if (!this.configManager.isEnabledToggle()) {
            player.sendMessage(this.configManager.getMessage("can_not_toggle"));
            return;
        }

        PlayerBoard playerBoard = this.boardManager.getPlayerBoard(player.getUniqueId());
        CompletableFuture<PlayerBoardData> future = this.boardManager.getPlayerBoardData(player.getUniqueId());
        future.whenComplete((playerBoardData, exception) -> Bukkit.getScheduler().runTask(this.plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (playerBoard != null) {
                this.boardManager.removePlayerBoard(player);
                playerBoardData.setToggled(false);
                this.boardManager.savePlayerBoardData(playerBoardData);
                player.sendMessage(this.configManager.getMessage("successfully_toggled_off"));
                return;
            }

            this.boardManager.addPlayerBoard(player);
            playerBoardData.setToggled(true);
            this.boardManager.savePlayerBoardData(playerBoardData);
            player.sendMessage(this.configManager.getMessage("successfully_toggled_on"));
        }));
    }
}
