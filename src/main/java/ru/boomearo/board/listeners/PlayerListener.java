package ru.boomearo.board.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoardData;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final Plugin plugin;
    private final BoardManager boardManager;

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        CompletableFuture<PlayerBoardData> future = this.boardManager.getPlayerBoardData(player.getUniqueId());
        future.whenComplete((playerBoardData, exception) -> Bukkit.getScheduler().runTask(this.plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (!playerBoardData.isToggled()) {
                return;
            }

            this.boardManager.addPlayerBoard(player);
        }));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        this.boardManager.removePlayerBoard(e.getPlayer());
    }

}
