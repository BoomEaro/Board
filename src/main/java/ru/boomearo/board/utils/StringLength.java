package ru.boomearo.board.utils;

import org.bukkit.Bukkit;
import ru.boomearo.board.Board;

public final class StringLength {

    private static int MAX_STRING_LENGTH = 16;

    public static void init(Board board) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

        MAX_STRING_LENGTH = calculateLength(version);

        board.getLogger().info("Используем максимальную длину строки в " + MAX_STRING_LENGTH + " символа. Определена версия: " + version);
    }

    private static int calculateLength(String version) {
        switch (version) {
            case "1_16_R3":
            case "1_17_R1": {
                return 64;
            }
            case "1_19_R3": {
                return Integer.MAX_VALUE;
            }
            default: {
                return 16;
            }
        }
    }

    public static int getMaxSupportedStringLength() {
        return MAX_STRING_LENGTH;
    }
}
