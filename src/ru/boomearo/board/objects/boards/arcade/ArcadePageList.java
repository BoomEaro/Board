package ru.boomearo.board.objects.boards.arcade;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.arcade.pages.ArcadeCityPage;
import ru.boomearo.board.objects.boards.arcade.pages.ArcadeMainPage;
import ru.boomearo.board.objects.boards.arcade.pages.ArcadePetPage;
import ru.boomearo.board.objects.boards.arcade.pages.ArcadeTechPage;

public class ArcadePageList extends AbstractPageList {

    public ArcadePageList(PlayerBoard player) {
        super(player);
    }

    @Override
    protected List<AbstractPage> createPages() {
        List<AbstractPage> pages = new ArrayList<AbstractPage>();
        
        pages.add(new ArcadeMainPage(this));
        pages.add(new ArcadeCityPage(this));
        pages.add(new ArcadePetPage(this));
        pages.add(new ArcadeTechPage(this));
        
        return pages;
    }

}
