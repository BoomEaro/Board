package ru.boomearo.board.objects;

import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.defaults.DefaultPageList;

public class DefaultPageListFactory implements IPageListFactory {

    @Override
    public AbstractPageList createPageList(PlayerBoard player) {
        return new DefaultPageList(player);
    }

}
