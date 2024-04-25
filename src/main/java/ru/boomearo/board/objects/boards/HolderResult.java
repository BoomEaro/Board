package ru.boomearo.board.objects.boards;

import lombok.Value;

@Value
public class HolderResult {

    String prefix;
    String suffix;

    public HolderResult(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
