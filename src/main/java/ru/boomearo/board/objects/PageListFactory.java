package ru.boomearo.board.objects;

import ru.boomearo.board.objects.boards.AbstractPageList;

public interface PageListFactory {

    public AbstractPageList createPageList(PlayerBoard player);

}
