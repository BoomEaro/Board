package ru.boomearo.board.objects;

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
}
