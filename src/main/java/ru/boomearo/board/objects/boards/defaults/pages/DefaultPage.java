package ru.boomearo.board.objects.boards.defaults.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.hooks.PlaceHolderAPIHook;
import ru.boomearo.board.objects.boards.AbstractTitleHolder;
import ru.boomearo.board.objects.boards.AbstractValueHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class DefaultPage extends AbstractPage {

    private final String title;
    private final List<String> teams;
    private final PlaceHolderAPIHook placeHolderAPIHook;

    public DefaultPage(AbstractPageList pageList, String title, List<String> teams, PlaceHolderAPIHook placeHolderAPIHook) {
        super(pageList);
        this.title = title;
        this.teams = teams;
        this.placeHolderAPIHook = placeHolderAPIHook;
    }

    @Override
    protected Duration getTimeToChange() {
        return Duration.ofMillis(Long.MAX_VALUE);
    }

    @Override
    protected boolean isVisible() {
        return true;
    }

    @Override
    protected AbstractTitleHolder createTitleHolder() {
        return new AbstractTitleHolder(this) {

            @Override
            protected String getText() {
                return DefaultPage.this.placeHolderAPIHook.replaceHolders(this.playerBoard.getPlayer(), DefaultPage.this.title);
            }

        };
    }

    @Override
    protected List<AbstractValueHolder> createTeamHolders() {
        List<AbstractValueHolder> tmpLines = new ArrayList<>();

        for (String team : this.teams) {
            tmpLines.add(new AbstractValueHolder(this) {

                @Override
                protected String getText() {
                    return DefaultPage.this.placeHolderAPIHook.replaceHolders(this.playerBoard.getPlayer(), team);
                }

            });
        }

        return tmpLines;
    }

}
