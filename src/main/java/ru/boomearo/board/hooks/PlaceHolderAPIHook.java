package ru.boomearo.board.hooks;

import org.bukkit.entity.Player;

public interface PlaceHolderAPIHook {

    String replaceHolders(Player player, String text);

}
