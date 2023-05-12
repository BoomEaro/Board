package ru.boomearo.board.utils;

import org.bukkit.Bukkit;
import ru.boomearo.board.Board;

public final class StringLength {

    private static DataLength MAX_DATA_LENGTH = new DataLength(16, 32);

    public static void init(Board board) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

        MAX_DATA_LENGTH = calculateLength(version);

        board.getLogger().info("Use the maximum string length " + MAX_DATA_LENGTH + ". Detected version: " + version);
    }

    private static DataLength calculateLength(String version) {
        switch (version) {
            case "1_16_R3":
            case "1_17_R1": {
                return new DataLength(64, 128);
            }
            default: {
                return new DataLength(16, 32);
            }
        }
    }

    public static DataLength getMaxDataLength() {
        return MAX_DATA_LENGTH;
    }

    public static class DataLength {
        private final int maxValueLength;
        private final int maxTitleLength;

        public DataLength(int maxValueLength, int maxTitleLength) {
            this.maxValueLength = maxValueLength;
            this.maxTitleLength = maxTitleLength;
        }

        public int getMaxValueLength() {
            return this.maxValueLength;
        }

        public int getMaxTitleLength() {
            return this.maxTitleLength;
        }

        @Override
        public String toString() {
            return "DataLength{" +
                    "maxValueLength=" + maxValueLength +
                    ", maxTitleLength=" + maxTitleLength +
                    '}';
        }
    }
}
