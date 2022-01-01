package ru.boomearo.board.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.ChatColor;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.objects.DefaultPageListFactory;
import ru.boomearo.board.objects.IPageListFactory;
import ru.boomearo.board.objects.PlayerBoard;

public final class BoardManager {

    public static final String prefix = "§8[§6Board§8]: §f";

    private final ConcurrentMap<String, PlayerBoard> playerBoards = new ConcurrentHashMap<>();

    private final Set<String> playersIgnore = new HashSet<>();

    private IPageListFactory factory = new DefaultPageListFactory();

    private final Object lock = new Object();

    public static final int maxEntrySize = 15;

    private static final List<String> entryNames = new ArrayList<>();

    static {
        //Инициализируем сразу же при создании класса названия
        for (ChatColor color : ChatColor.values()) {
            entryNames.add("" + color + ChatColor.RESET);
        }
    }

    public IPageListFactory getPageListFactory() {
        return this.factory;
    }

    public void setPageListFactory(IPageListFactory factory) {
        this.factory = factory;

        forceApplyPageListToPlayers();
    }

    public void forceApplyPageListToPlayers() {
        for (PlayerBoard pb : this.playerBoards.values()) {
            try {
                pb.setNewPageList(this.factory.createPageList(pb));
            }
            catch (BoardException e) {
                e.printStackTrace();
            }
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
            return new ArrayList<>(this.playersIgnore);
        }
    }

    public static String getColor(int index) {
        return entryNames.get(index);
    }

}
