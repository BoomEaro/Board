package ru.boomearo.board.objects.boards.test;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.test.pages.TestMainPage;
import ru.boomearo.board.objects.boards.test.pages.TestTechPage;

public class TestPageList extends AbstractPageList {

    public TestPageList(PlayerBoard player) {
        super(player);
    }

    @Override
    protected List<AbstractPage> createPages() {
        List<AbstractPage> tmpBoards = new ArrayList<AbstractPage>();

        tmpBoards.add(new TestMainPage(this));
        tmpBoards.add(new TestTechPage(this));
        
        return tmpBoards;
    }
}
