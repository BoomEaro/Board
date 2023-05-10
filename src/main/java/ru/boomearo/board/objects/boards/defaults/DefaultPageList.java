package ru.boomearo.board.objects.boards.defaults;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.defaults.pages.DefaultFirstPage;
import ru.boomearo.board.objects.boards.defaults.pages.DefaultSecondPage;

public class DefaultPageList extends AbstractPageList {

    public DefaultPageList(PlayerBoard player) {
        super(player);
    }

    @Override
    protected List<AbstractPage> createPages() {
        List<AbstractPage> pages = new ArrayList<>();

        pages.add(new DefaultFirstPage(this));
        pages.add(new DefaultSecondPage(this));

        return pages;
    }

}
