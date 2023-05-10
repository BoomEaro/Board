package ru.boomearo.board.objects.boards.defaults.pages;

import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.objects.boards.AbstractTitleHolder;
import ru.boomearo.board.objects.boards.AbstractValueHolder;
import ru.boomearo.board.objects.boards.DefaultScoreSequenceFactory;
import ru.boomearo.board.objects.boards.ScoreSequenceFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultSecondPage extends AbstractPage {

    public DefaultSecondPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    protected ScoreSequenceFactory getScoreSequenceFactory() {
        return DefaultScoreSequenceFactory.TO_NEGATIVE_ZERO;
    }


    @Override
    protected long getTimeToChange() {
        return 1000 * 5;
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
                return "§cSecond test page " + this.test;
            }

        };
    }

    @Override
    protected List<AbstractValueHolder> createTeamHolders() {
        List<AbstractValueHolder> tmpLines = new ArrayList<>();
        tmpLines.add(new AbstractValueHolder(this) {

            @Override
            protected String getText() {
                return "§6Test board";
            }

        });
        tmpLines.add(new AbstractValueHolder(this) {

            private boolean test = false;

            @Override
            protected String getText() {
                this.test = !this.test;
                return (this.test ? "§aFirst" : "§cSecond");
            }

            @Override
            protected long getMaxCacheTime() {
                return 500;
            }

        });

        return tmpLines;
    }

}
