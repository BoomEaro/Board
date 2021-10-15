package ru.boomearo.board.objects;

import ru.boomearo.board.objects.boards.AbstractPageList;

public interface IPageListFactory {

    public AbstractPageList createPageList(PlayerBoard player);

}
