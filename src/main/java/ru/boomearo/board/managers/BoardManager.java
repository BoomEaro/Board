package ru.boomearo.board.managers;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
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
import ru.boomearo.board.objects.PageListFactory;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.PlayerToggle;
import ru.boomearo.board.tasks.BoardUpdateTask;

public final class BoardManager {

    private final ConcurrentMap<UUID, PlayerBoard> playerBoards = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, PlayerToggle> playersToggle = new ConcurrentHashMap<>();

    private PageListFactory factory = new DefaultPageListFactory();

    private boolean defaultToggle = true;
    private boolean enabledToggle = true;

    private ScheduledExecutorService scheduler = null;

    public static final String PREFIX = "§8[§6Board§8]: §f";
    public static final int MAX_ENTRY_SIZE = 15;
    public static final String TEAM_PREFIX = "BoardT_";

    private static final String[] ENTRY_NAMES;

    static {
        ChatColor[] chatColors = ChatColor.values();
        String[] entryNames = new String[chatColors.length];
        for (int i = 0; i < chatColors.length; i++) {
            entryNames[i] = "" + chatColors[i] + ChatColor.RESET;
        }
        ENTRY_NAMES = entryNames;
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
            playersConfig.set("players." + pt.getUuid().toString() + ".toggle", pt.isToggle());
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

            ConcurrentMap<UUID, PlayerToggle> tmp = new ConcurrentHashMap<>();

            ConfigurationSection cs = playersConfig.getConfigurationSection("players");
            if (cs != null) {
                for (String uuidString : cs.getKeys(false)) {
                    boolean toggle = playersConfig.getBoolean("players." + uuidString + ".toggle");
                    UUID uuid = UUID.fromString(uuidString);

                    tmp.put(uuid, new PlayerToggle(uuid, toggle));
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
            PlayerToggle pt = getOrCreatePlayerToggle(pl);
            if (pt.isToggle()) {
                addPlayerBoard(pl);
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

        this.scheduler.scheduleAtFixedRate(new BoardUpdateTask(this), 100, 100, TimeUnit.MILLISECONDS);
    }

    private void unloadScheduler() {
        if (this.scheduler == null) {
            return;
        }

        this.scheduler.shutdownNow();
        this.scheduler = null;
    }

    public PageListFactory getPageListFactory() {
        return this.factory;
    }

    public void setPageListFactory(PageListFactory factory) {
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

    public PlayerBoard getPlayerBoard(UUID uuid) {
        return this.playerBoards.get(uuid);
    }

    public void addPlayerBoard(Player player) {
        PlayerBoard pb = this.playerBoards.get(player.getUniqueId());
        if (pb != null) {
            return;
        }

        PlayerBoard playerBoard = new PlayerBoard(player.getUniqueId(), player, this);

        this.playerBoards.put(player.getUniqueId(), playerBoard);

        try {
            playerBoard.init();
            sendBoardToPlayer(playerBoard, null);
        }
        catch (BoardException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayerBoard(Player player) {
        PlayerBoard pb = this.playerBoards.get(player.getUniqueId());
        if (pb == null) {
            return;
        }

        this.playerBoards.remove(player.getUniqueId());
        pb.remove();
    }

    public Collection<PlayerBoard> getAllPlayerBoards() {
        return this.playerBoards.values();
    }


    public PlayerToggle getOrCreatePlayerToggle(Player player) {
        return this.playersToggle.computeIfAbsent(player.getUniqueId(), (value) -> new PlayerToggle(value, this.defaultToggle));
    }

    /**
     * Устанавливает скорборд указанному игроку с указанной фабрикой страниц.
     * Если указанный игрок не был онлайн или скорборд был выключен, ничего не произойдет.
     * @param Player Игрок
     * @param factory Фабрика страниц. null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
     */
    public void sendBoardToPlayer(Player player, PageListFactory factory) {
        if (player == null) {
            throw new IllegalStateException("Player can not be null!");
        }
        sendBoardToPlayer(player.getUniqueId(), factory);
    }

    /**
     * Устанавливает скорборд указанному игроку с указанной фабрикой страниц.
     * Если указанный игрок не был онлайн или скорборд был выключен, ничего не произойдет.
     * @param uuid uuid игрока
     * @param factory Фабрика страниц. null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
     */
    public void sendBoardToPlayer(UUID uuid, PageListFactory factory) {
        if (uuid == null) {
            throw new IllegalStateException("UUID can not be null!");
        }
        PlayerBoard pb = this.playerBoards.get(uuid);
        if (pb == null) {
            throw new IllegalStateException("Player with uuid '" + uuid + "' does not exists!");
        }
        sendBoardToPlayer(pb, factory);
    }

    /**
     * Устанавливает скорборд указанному игроку с указанной фабрикой страниц.
     * Если указанный игрок не был онлайн или скорборд был выключен, ничего не произойдет.
     * @param PlayerBoard PlayerBoard игрока
     * @param factory Фабрика страниц. null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
     */
    public void sendBoardToPlayer(PlayerBoard playerBoard, PageListFactory factory) {
        if (playerBoard == null) {
            throw new IllegalStateException("PlayerBoard can not be null!");
        }
        try {
            if (factory == null) {
                factory = getPageListFactory();
            }

            playerBoard.setNewPageList(factory.createPageList(playerBoard));
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
        return ENTRY_NAMES[index];
    }

}
