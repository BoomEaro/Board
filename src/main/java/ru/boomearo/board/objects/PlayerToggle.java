package ru.boomearo.board.objects;

import java.util.Objects;

public class PlayerToggle {

    private final String name;
    private boolean toggle;

    public PlayerToggle(String name, boolean toggle) {
        this.name = name;
        this.toggle = toggle;
    }

    public String getName() {
        return this.name;
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
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "PlayerToggle{" +
                "name='" + name + '\'' +
                ", toggle=" + toggle +
                '}';
    }
}
