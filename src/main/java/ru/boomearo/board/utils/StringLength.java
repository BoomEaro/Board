package ru.boomearo.board.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import ru.boomearo.board.Board;

@UtilityClass
public class StringLength {

    private static DataLength MAX_DATA_LENGTH = new DataLength(16, 32);

    public static void init(Board board) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

        MAX_DATA_LENGTH = calculateLength(version);

        board.getLogger().info("Use the maximum string length " + MAX_DATA_LENGTH + ". Detected version: " + version);
    }

    private static DataLength calculateLength(String version) {
        return switch (version) {
            case "1_16_R3", "1_17_R1", "1_18_R1", "1_19_R3", "1_20_R2" -> new DataLength(64, 128);
            default -> new DataLength(16, 32);
        };
    }

    public static DataLength getMaxDataLength() {
        return MAX_DATA_LENGTH;
    }

}
