package ru.boomearo.board.objects.boards;

import java.util.Collections;
import java.util.List;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.objects.PlayerBoard;

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

    public void loadPages() throws BoardException {
        List<AbstractPage> tmp;
        try {
            tmp = createPages();
        }
        catch (Exception e) {
            throw new BoardException(e.getMessage());
        }

        if (tmp == null) {
            throw new BoardException("Page list can not be null!");
        }

        if (tmp.isEmpty()) {
            throw new BoardException("Page list can not be empty!");
        }

        for (AbstractPage ap : tmp) {
            ap.loadTitleHolder();
            ap.loadTeamHolders();
        }

        this.pages = Collections.unmodifiableList(tmp);
    }

    protected abstract List<AbstractPage> createPages();
}
