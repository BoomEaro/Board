package ru.boomearo.board.objects.boards.defaults;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.hooks.PlaceHolderAPIHook;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.defaults.pages.DefaultPage;

public class DefaultPageList extends AbstractPageList {

    private final String title;
    private final List<String> teams;
    private final PlaceHolderAPIHook placeHolderAPIHook;

    public DefaultPageList(PlayerBoard player, String title, List<String> teams, PlaceHolderAPIHook placeHolderAPIHook) {
        super(player);
        this.title = title;
        this.teams = teams;
        this.placeHolderAPIHook = placeHolderAPIHook;
    }

    @Override
    protected List<AbstractPage> createPages() {
        List<AbstractPage> pages = new ArrayList<>();

        pages.add(new DefaultPage(this, this.title, this.teams, this.placeHolderAPIHook));

        return pages;
    }

}
