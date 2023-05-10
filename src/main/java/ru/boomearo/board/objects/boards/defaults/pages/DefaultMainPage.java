package ru.boomearo.board.objects.boards.defaults.pages;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.boards.AbstractTitleHolder;
import ru.boomearo.board.objects.boards.AbstractValueHolder;
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
    protected AbstractTitleHolder createTitleHolder() {
        return new AbstractTitleHolder(this) {

            private int test = 1;

            @Override
            protected String getText() {
                this.test++;
                return "§cEmpty board Test " + this.test;
            }

        };
    }

    @Override
    protected List<AbstractValueHolder> createTeamHolders() {
        List<AbstractValueHolder> tmpLines = new ArrayList<>();
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            public String getText() {
                return "§cTest board";
            }

        });
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            public String getText() {
                return "§cTest2";
            }

        });
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            public String getText() {
                return "§aCurrent HP " + this.playerBoard.getPlayer().getHealth();
            }
        });
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            public String getText() {
                return "§6Current hunger " + this.playerBoard.getPlayer().getFoodLevel();
            }
        });

        return tmpLines;
    }

}
