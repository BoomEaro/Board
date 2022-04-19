package ru.boomearo.board.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;

public class PlayerListener implements Listener {

    private final BoardManager boardManager;

    public PlayerListener(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player pl = e.getPlayer();

        PlayerToggle pt = this.boardManager.getOrCreatePlayerToggle(pl.getName());
        if (pt.isToggle()) {
            PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
            if (pb == null) {
                this.boardManager.addPlayerBoard(new PlayerBoard(pl));
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player pl = e.getPlayer();

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getName());
        if (pb != null) {
            this.boardManager.removePlayerBoard(pl.getName());
        }
    }

}
