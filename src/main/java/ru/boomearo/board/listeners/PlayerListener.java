package ru.boomearo.board.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.boomearo.board.Board;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player pl = e.getPlayer();

        BoardManager manager = Board.getInstance().getBoardManager();
        if (!manager.isIgnore(pl.getName())) {
            PlayerBoard pb = manager.getPlayerBoard(pl.getName());
            if (pb == null) {
                manager.addPlayerBoard(new PlayerBoard(pl));
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player pl = e.getPlayer();

        BoardManager manager = Board.getInstance().getBoardManager();
        if (!manager.isIgnore(pl.getName())) {
            PlayerBoard pb = manager.getPlayerBoard(pl.getName());
            if (pb != null) {
                manager.removePlayerBoard(pl.getName());
            }
        }
    }

}
