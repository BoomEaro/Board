package ru.boomearo.board.objects.boards.defaults.pages;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.boards.AbstractTitleHolder;
import ru.boomearo.board.objects.boards.AbstractValueHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class DefaultFirstPage extends AbstractPage {

    public DefaultFirstPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    protected long getTimeToChange() {
        return 1000 * 15;
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
                return "§cFirst test page " + this.test;
            }

        };
    }

    @Override
    protected List<AbstractValueHolder> createTeamHolders() {
        List<AbstractValueHolder> tmpLines = new ArrayList<>();
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            protected String getText() {
                return "§cTest board";
            }

        });
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            protected String getText() {
                double health = this.playerBoard.getPlayer().getHealth();
                if (health < 20) {
                    return "§aCurrent HP " + health;
                }
                return null;
            }
        });
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            protected String getText() {
                double food = this.playerBoard.getPlayer().getFoodLevel();
                if (food < 20) {
                    return "§6Current hunger " + food;
                }
                return null;
            }
        });

        return tmpLines;
    }

}
