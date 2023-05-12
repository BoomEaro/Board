package ru.boomearo.board.objects;

import ru.boomearo.board.hooks.PlaceHolderAPIHook;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.defaults.DefaultPageList;

import java.util.List;

public class DefaultPageListFactory implements PageListFactory {

    private final String title;
    private final List<String> teams;
    private final PlaceHolderAPIHook placeHolderAPIHook;

    public DefaultPageListFactory(String title, List<String> teams, PlaceHolderAPIHook placeHolderAPIHook) {
        this.title = title;
        this.teams = teams;
        this.placeHolderAPIHook = placeHolderAPIHook;
    }

    @Override
    public AbstractPageList createPageList(PlayerBoard player) {
        return new DefaultPageList(player, this.title, this.teams, this.placeHolderAPIHook);
    }

}
