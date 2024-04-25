package ru.boomearo.board.objects;

import lombok.Data;
import java.util.UUID;

@Data
public class PlayerToggle {

    private final UUID uuid;
    private boolean toggle;

    public PlayerToggle(UUID uuid, boolean toggle) {
        this.uuid = uuid;
        this.toggle = toggle;
    }
}
