package ru.boomearo.board.objects.boards;

import java.util.Objects;

public class HolderResult {

    private final String prefix;
    private final String suffix;

    public HolderResult(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolderResult that = (HolderResult) o;
        return Objects.equals(prefix, that.prefix) && Objects.equals(suffix, that.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, suffix);
    }

    @Override
    public String toString() {
        return "HolderData{" +
                "prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
