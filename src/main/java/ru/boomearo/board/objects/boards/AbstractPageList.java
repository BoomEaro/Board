package ru.boomearo.board.objects.boards;

import ru.boomearo.board.objects.PlayerBoard;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPageList {

    protected final PlayerBoard playerBoard;

    private List<AbstractPage> pages;

    public AbstractPageList(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    public List<AbstractPage> getPages() {
        return this.pages;
    }

    public void loadPages() {
        List<AbstractPage> tmp;
        try {
            tmp = createPages();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        if (tmp == null) {
            throw new IllegalStateException("Page list can not be null!");
        }

        if (tmp.isEmpty()) {
            throw new IllegalStateException("Page list can not be empty!");
        }

        for (AbstractPage ap : tmp) {
            ap.loadTitleHolder();
            ap.loadTeamHolders();
        }

        this.pages = Collections.unmodifiableList(tmp);
    }

    protected abstract List<AbstractPage> createPages();
}
