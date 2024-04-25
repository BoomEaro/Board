package ru.boomearo.board.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.database.DatabaseRepository;
import ru.boomearo.board.hooks.PlaceHolderAPIHook;
import ru.boomearo.board.objects.*;
import ru.boomearo.board.tasks.BalancedThreadPool;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

public final class BoardManager {

    public static final int MAX_ENTRY_SIZE = 15;
    public static final String TEAM_PREFIX = "BoardT_";

    private static final String[] ENTRY_NAMES;

    private final Plugin plugin;
    private final ConfigManager configManager;
    private final DatabaseRepository databaseRepository;
    private final PlaceHolderAPIHook placeHolderAPIHook;

    private final ConcurrentMap<UUID, PlayerBoard> playerBoards = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, PlayerBoardData> playerBoardData = new ConcurrentHashMap<>();

    private PageListFactory factory = null;
    private BalancedThreadPool balancedThreadPool = null;

    public BoardManager(Plugin plugin, ConfigManager configManager, DatabaseRepository databaseRepository, PlaceHolderAPIHook placeHolderAPIHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.databaseRepository = databaseRepository;
        this.placeHolderAPIHook = placeHolderAPIHook;
        this.factory = getDefaultPageListFactory();
    }

    static {
        ChatColor[] chatColors = ChatColor.values();
        String[] entryNames = new String[chatColors.length];
        for (int i = 0; i < chatColors.length; i++) {
            entryNames[i] = String.valueOf(chatColors[i]) + ChatColor.RESET;
        }
        ENTRY_NAMES = entryNames;
    }

    public void load() {
        loadPlayerBoards();
        loadExecutor();
    }

    public void unload() {
        unloadPlayerBoards();
        unloadExecutor();
    }

    public CompletableFuture<PlayerBoardData> getPlayerBoardData(UUID uuid) {
        PlayerBoardData data = this.playerBoardData.get(uuid);
        if (data != null) {
            return CompletableFuture.completedFuture(data);
        }

        CompletableFuture<PlayerBoardData> future = new CompletableFuture<>();
        this.databaseRepository.getPlayerBoardData(uuid, (player) -> {
            if (player == null) {
                PlayerBoardData newData = new PlayerBoardData(uuid, this.configManager.isDefaultToggle());
                this.playerBoardData.put(uuid, newData);
                this.databaseRepository.insertOrUpdatePlayer(newData);
                future.complete(newData);
                return;
            }

            this.playerBoardData.put(uuid, player);
            future.complete(player);
        });
        return future;
    }

    public void savePlayerBoardData(PlayerBoardData playerBoardData) {
        this.databaseRepository.insertOrUpdatePlayer(playerBoardData);
    }

    public void unloadPlayerBoards() {
        for (PlayerBoard pb : this.playerBoards.values()) {
            pb.remove();
        }
        this.playerBoards.clear();
    }

    public void loadPlayerBoards() {
        if (!this.configManager.isDefaultToggle()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            CompletableFuture<PlayerBoardData> pt = getPlayerBoardData(player.getUniqueId());
            pt.whenComplete((playerBoardData, exception) -> Bukkit.getScheduler().runTask(this.plugin, () -> {
                if (!player.isOnline()) {
                    return;
                }
                if (!playerBoardData.isToggled()) {
                    return;
                }
                addPlayerBoard(player);
            }));
        }
    }

    public void loadExecutor() {
        if (this.balancedThreadPool != null) {
            return;
        }

        int threads = this.configManager.getThreadPool();
        if (threads < 1) {
            threads = 1;
        }

        this.balancedThreadPool = new BalancedThreadPool("Board", threads);
    }

    public void unloadExecutor() {
        if (this.balancedThreadPool == null) {
            return;
        }

        this.balancedThreadPool.shutdown();
        this.balancedThreadPool = null;
    }

    public PageListFactory getPageListFactory() {
        return this.factory;
    }

    public void setPageListFactory(PageListFactory factory) {
        setPageListFactory(factory, true);
    }

    public void setPageListFactory(PageListFactory factory, boolean force) {
        this.factory = factory;

        if (force) {
            forceApplyPageListToPlayers();
        }
    }

    public void resetPageListFactory() {
        this.factory = getDefaultPageListFactory();

        forceApplyPageListToPlayers();
    }

    public void forceApplyPageListToPlayers() {
        for (PlayerBoard pb : this.playerBoards.values()) {
            try {
                pb.setNewPageList(this.factory.createPageList(pb));
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to force apply page for player " + pb.getPlayer().getName(), e);
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

        PlayerBoardImpl playerBoard = new PlayerBoardImpl(player.getUniqueId(), player, this.plugin, this);

        this.playerBoards.put(player.getUniqueId(), playerBoard);

        try {
            playerBoard.init();
            int update = this.configManager.getUpdateFreq();
            if (update < 1) {
                update = 1;
            }

            playerBoard.bindUsedExecutor(update, this.balancedThreadPool.getFreeExecutor());

            sendBoardToPlayer(playerBoard, null);
        } catch (Exception e) {
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

    /**
     * Устанавливает скорборд указанному игроку с указанной фабрикой страниц.
     * Если указанный игрок не был онлайн или скорборд был выключен, ничего не произойдет.
     *
     * @param player  Игрок
     * @param factory Фабрика страниц. Null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
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
     *
     * @param uuid    uuid игрока
     * @param factory Фабрика страниц. Null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
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
     *
     * @param playerBoard PlayerBoard игрока
     * @param factory     Фабрика страниц. Null значение сбросит фабрику страниц до реализации по умолчанию зарегистрированной у Board.
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
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to send board to player " + playerBoard.getPlayer().getName(), e);
        }
    }

    public PageListFactory getDefaultPageListFactory() {
        return new DefaultPageListFactory(this.configManager.getBoardTitle(), this.configManager.getBoardTeams(), this.placeHolderAPIHook);
    }

    public static String getColor(int index) {
        return ENTRY_NAMES[index];
    }

}
