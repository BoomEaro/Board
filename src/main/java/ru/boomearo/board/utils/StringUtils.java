package ru.boomearo.board.utils;

import org.bukkit.Bukkit;
import ru.boomearo.board.Board;

public final class StringUtils {

    private static final int maxStringLength;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

        maxStringLength = calculateLength(version);

        Board.getInstance().getLogger().info("Используем максимальную длину строки в " + maxStringLength + " символа. Определена версия: " + version);
    }

    private static int calculateLength(String version) {
        switch (version) {
            case "1_16_R3":
            case "1_17_R1": {
                return 64;
            }
            case "1_18_R1": {
                return Integer.MAX_VALUE;
            }
            default: {
                return 16;
            }
        }
    }


    public static int getMaxSupportedStringLength() {
        return maxStringLength;
    }
}
