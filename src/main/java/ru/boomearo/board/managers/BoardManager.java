package ru.boomearo.board.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import ru.boomearo.board.tasks.BoardUpdateTask;

public final class BoardManager {

    private final ConcurrentMap<String, PlayerBoard> playerBoards = new ConcurrentHashMap<>();
    private ConcurrentMap<String, PlayerToggle> playersToggle = new ConcurrentHashMap<>();

    private IPageListFactory factory = new DefaultPageListFactory();
    private boolean defaultToggle = true;
    private boolean enabledToggle = true;

    private ScheduledExecutorService scheduler = null;

    public static final String PREFIX = "§8[§6Board§8]: §f";
    public static final int MAX_ENTRY_SIZE = 15;

    private static final List<String> ENTRY_NAMES = new ArrayList<>();

    static {
        //Инициализируем сразу же при создании класса названия
        for (ChatColor color : ChatColor.values()) {
            ENTRY_NAMES.add("" + color + ChatColor.RESET);
        }
    }

    public void load() {
        loadConfig();
        loadPlayersConfig();
        loadPlayerBoards();
        loadScheduler();
    }

    public void unload() {
        unloadPlayerBoards();
        savePlayersConfig();
        unloadScheduler();
    }

    public void savePlayersConfig() {
        //Если переключение выключено значит не сохраняем конфиг
        if (!this.isEnabledToggle()) {
            return;
        }
        File playersConfigFile = new File(Board.getInstance().getDataFolder(), "players.yml");
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

    private void unloadPlayerBoards() {
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

    private void loadPlayersConfig() {
        File playersConfigFile;
        FileConfiguration playersConfig;
        playersConfigFile = new File(Board.getInstance().getDataFolder(), "players.yml");
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

    private void loadPlayerBoards() {
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

    private void loadScheduler() {
        if (this.scheduler != null) {
            return;
        }

        this.scheduler = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder()
                .setNameFormat("Board-%d")
                .setPriority(Thread.MIN_PRIORITY)
                .build());

        this.scheduler.scheduleAtFixedRate(new BoardUpdateTask(this), 1, 1, TimeUnit.SECONDS);
    }

    private void unloadScheduler() {
        if (this.scheduler == null) {
            return;
        }

        this.scheduler.shutdownNow();
        this.scheduler = null;
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

    /**
     * Устанавливает скорборд указанному игроку с указанной фабрикой страниц.
     * Если указанный игрок не был онлайн или скорборд был выключен, ничего не произойдет.
     * @param name Ник игрока
     * @param factory Фабрика страниц. null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
     */
    public void sendBoardToPlayer(String name, IPageListFactory factory) {
        PlayerBoard pb = getPlayerBoard(name);
        if (pb == null) {
            return;
        }
        try {
            if (factory == null) {
                factory = getPageListFactory();
            }

            pb.setNewPageList(factory.createPageList(pb));
        }
        catch (BoardException e) {
            e.printStackTrace();
        }
    }

    public boolean isDefaultToggle() {
        return this.defaultToggle;
    }

    public boolean isEnabledToggle() {
        return this.enabledToggle;
    }

    public static String getColor(int index) {
        return ENTRY_NAMES.get(index);
    }

}
