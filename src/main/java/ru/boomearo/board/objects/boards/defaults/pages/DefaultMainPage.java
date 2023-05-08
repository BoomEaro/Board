package ru.boomearo.board.objects.boards.defaults.pages;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class DefaultMainPage extends AbstractPage {

    public DefaultMainPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    protected int getTimeToChange() {
        return 10;
    }

    @Override
    protected boolean isVisible() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "§cEmpty board";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§cJust test";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§cTest2";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§aCurrent HP " + this.playerBoard.getPlayer().getHealth();
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Current hunger " + this.playerBoard.getPlayer().getFoodLevel();
            }
        });

        return tmpLines;
    }

}
