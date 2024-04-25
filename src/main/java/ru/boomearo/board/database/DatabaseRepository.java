package ru.boomearo.board.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import org.sqlite.JDBC;
import ru.boomearo.board.objects.PlayerBoardData;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

@RequiredArgsConstructor
public class DatabaseRepository {

    private final Plugin plugin;
    private Connection connection;
    private ExecutorService executor;

    private static final String CON_STR = "jdbc:sqlite:[path]database.db";

    @SneakyThrows
    public void load() {
        if (this.connection != null) {
            return;
        }

        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        DriverManager.registerDriver(new JDBC());

        this.executor = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder()
                .setNameFormat("Board-SQL")
                .setPriority(3)
                .build());

        this.connection = DriverManager.getConnection(CON_STR.replace("[path]", this.plugin.getDataFolder() + File.separator));

        createNewDatabase();
    }

    @SneakyThrows
    public void unload() {
        if (this.connection == null) {
            return;
        }

        this.executor.shutdown();
        this.executor.awaitTermination(15, TimeUnit.SECONDS);
        this.connection.close();
    }

    public void getPlayerBoardData(UUID uuid, Consumer<PlayerBoardData> consumer) {
        this.executor.execute(() -> {
            try (PreparedStatement statement = this.connection.prepareStatement("SELECT toggled FROM players WHERE uuid = ? LIMIT 1")) {
                statement.setString(1, uuid.toString());

                ResultSet resSet = statement.executeQuery();

                if (resSet.next()) {
                    consumer.accept(new PlayerBoardData(uuid, resSet.getBoolean("toggled")));
                    return;
                }
                consumer.accept(null);
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to get player section", e);
                consumer.accept(null);
            }
        });
    }

    public void insertOrUpdatePlayer(PlayerBoardData boardData) {
        this.executor.execute(() -> {
            String sql = "REPLACE INTO players (uuid, toggled)" +
                    "VALUES(?, ?)";
            try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
                statement.setString(1, boardData.getUuid().toString());
                statement.setBoolean(2, boardData.isToggled());
                statement.execute();
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to insert player section", e);
            }
        });
    }

    private void createNewDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS players ("
                + " uuid VARCHAR(255) PRIMARY KEY NOT NULL,"
                + " toggled BOOLEAN NOT NULL"
                + ");";

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to create players database", e);
        }
    }

}
