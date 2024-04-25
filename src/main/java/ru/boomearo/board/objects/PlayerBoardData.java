package ru.boomearo.board.objects;

import lombok.Data;
import java.util.UUID;

@Data
public class PlayerBoardData {

    private final UUID uuid;
    private boolean toggled;

    public PlayerBoardData(UUID uuid, boolean toggled) {
        this.uuid = uuid;
        this.toggled = toggled;
    }
}
