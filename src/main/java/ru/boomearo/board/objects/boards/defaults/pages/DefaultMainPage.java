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
        return "§cПустой борд";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§cЗдесь что то должно быть";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§cНо почему то этого нет.";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                double h = getPageList().getPlayerBoard().getPlayer().getHealth();
                if (h > 10) {
                    return "У вас больше 10 хп!1";
                }
                else {
                    return "";
                }
            }
        });

        return tmpLines;
    }

}
