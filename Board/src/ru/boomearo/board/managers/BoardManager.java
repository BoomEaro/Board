package ru.boomearo.board.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.ChatColor;

import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractBoard;

public final class BoardManager {

    public static final String prefix = "§8[§6Board§8]: §f";

    private final ConcurrentMap<String, PlayerBoard> playerBoards = new ConcurrentHashMap<String, PlayerBoard>();

    private final Set<String> playersIgnore = new HashSet<String>();

    private AbstractBoard board = null;

    private final Object lock = new Object();

    private static List<String> entryNames = new ArrayList<String>();
    static {
        //Инициализируем сразу же при создании класса названия
        for (ChatColor color : ChatColor.values()) {
            entryNames.add("" + color + ChatColor.RESET);
        }
    }

    public PlayerBoard getPlayerBoard(String player) {
        return this.playerBoards.get(player);
    }
    public void addPlayerBoard(PlayerBoard board) {
        this.playerBoards.put(board.getPlayer().getName(), board);
    }
    public void removePlayerBoard(String player) {
        PlayerBoard pb = this.playerBoards.get(player);
        if (pb != null) {
            this.playerBoards.remove(player);
            pb.remove();
        }
    }
    public Collection<PlayerBoard> getAllPlayerBoards() {
        return this.playerBoards.values();
    }

    public AbstractBoard getBoard() {
        return this.board;
    }

    public void setBoard(AbstractBoard board) {
        this.board = board;
    }

    public boolean isIgnore(String player) {
        synchronized (this.lock) {
            return this.playersIgnore.contains(player);
        }
    }

    public void addIgnore(String player) {
        synchronized (this.lock) {
            this.playersIgnore.add(player);
        }
    }
    public void removeIgnore(String player) {
        synchronized (this.lock) {
            this.playersIgnore.remove(player);
        }
    }

    public List<String> getAllIgnores() {
        synchronized (this.lock) {
            return new ArrayList<String>(this.playersIgnore);
        }
    }

    public static String getColor(int index) {
        return entryNames.get(index);
    }

}
