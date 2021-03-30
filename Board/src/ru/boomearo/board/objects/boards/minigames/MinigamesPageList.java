package ru.boomearo.board.objects.boards.minigames;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.test.pages.TestMainPage;
import ru.boomearo.board.objects.boards.test.pages.TestTechPage;

public class MinigamesPageList extends AbstractPageList {

    public MinigamesPageList(PlayerBoard player) {
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
