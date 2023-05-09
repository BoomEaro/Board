package ru.boomearo.board.objects;

import java.util.Objects;
import java.util.UUID;

public class PlayerToggle {

    private final UUID uuid;
    private boolean toggle;

    public PlayerToggle(UUID uuid, boolean toggle) {
        this.uuid = uuid;
        this.toggle = toggle;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isToggle() {
        return this.toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerToggle that = (PlayerToggle) o;
        return toggle == that.toggle && Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, toggle);
    }

    @Override
    public String toString() {
        return "PlayerToggle{" +
                "uuid=" + uuid +
                ", toggle=" + toggle +
                '}';
    }
}
