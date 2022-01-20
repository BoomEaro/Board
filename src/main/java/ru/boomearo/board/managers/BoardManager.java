package ru.boomearo.board.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ru.boomearo.board.Board;
import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.objects.DefaultPageListFactory;
import ru.boomearo.board.objects.IPageListFactory;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;

public final class BoardManager {

    private final ConcurrentMap<String, PlayerBoard> playerBoards = new ConcurrentHashMap<>();
    private ConcurrentMap<String, PlayerToggle> playersToggle = new ConcurrentHashMap<>();

    private IPageListFactory factory = new DefaultPageListFactory();
    private boolean defaultToggle = true;
    private boolean enabledToggle = true;

    public static final String prefix = "§8[§6Board§8]: §f";
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

    public void resetPageListFactory() {
        this.factory = new DefaultPageListFactory();

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


    public PlayerToggle getOrCreatePlayerToggle(String name) {
        return this.playersToggle.computeIfAbsent(name, (value) -> new PlayerToggle(value, this.defaultToggle));
    }

    public void loadPlayersConfig() {
        File playersConfigFile;
        FileConfiguration playersConfig;
        playersConfigFile = new File( Board.getInstance().getDataFolder(), "players.yml");
        if (!playersConfigFile.exists()) {
            Board.getInstance().getLogger().info("Конфигурация игроков не найдена, создаем новую..");
            playersConfigFile.getParentFile().mkdirs();
            Board.getInstance().saveResource("players.yml", false);
        }

        playersConfig = new YamlConfiguration();

        try {
            playersConfig.load(playersConfigFile);

            ConcurrentMap<String, PlayerToggle> tmp = new ConcurrentHashMap<>();

            ConfigurationSection cs = playersConfig.getConfigurationSection("players");
            if (cs != null) {
                for (String name : cs.getKeys(false)) {
                    boolean toggle = playersConfig.getBoolean("players." + name + ".toggle");
                    tmp.put(name, new PlayerToggle(name, toggle));
                }
            }

            this.playersToggle = tmp;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayersConfig() {
        //Если переключение выключено значит не сохраняем конфиг
        if (!this.isEnabledToggle()) {
            return;
        }
        File playersConfigFile = new File( Board.getInstance().getDataFolder(), "players.yml");
        FileConfiguration playersConfig = new YamlConfiguration();

        for (PlayerToggle pt : this.playersToggle.values()) {
            playersConfig.set("players." + pt.getName() + ".toggle", pt.isToggle());
        }

        try {
            playersConfig.save(playersConfigFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerBoards() {
        if (!this.defaultToggle) {
            return;
        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            PlayerToggle pt = getOrCreatePlayerToggle(pl.getName());
            if (pt.isToggle()) {
                addPlayerBoard(new PlayerBoard(pl));
            }
        }
    }

    public void unloadPlayerBoards() {
        for (PlayerBoard pb : this.playerBoards.values()) {
            pb.remove();
        }
    }

    public void loadConfig() {
        Board.getInstance().reloadConfig();
        FileConfiguration fc = Board.getInstance().getConfig();
        this.defaultToggle = fc.getBoolean("defaultToggle");
        this.enabledToggle = fc.getBoolean("enabledToggle");
    }

    public boolean isDefaultToggle() {
        return this.defaultToggle;
    }

    public boolean isEnabledToggle() {
        return this.enabledToggle;
    }

    public static String getColor(int index) {
        return entryNames.get(index);
    }


}
