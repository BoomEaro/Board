package ru.boomearo.board.objects.boards;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPage {

    protected final AbstractPageList pageList;
    protected final PlayerBoard playerBoard;

    private AbstractTitleHolder loadedTitleHolder;
    private List<AbstractValueHolder> loadedTeamHolders;

    public AbstractPage(AbstractPageList pageList) {
        this.pageList = pageList;
        this.playerBoard = this.pageList.playerBoard;
    }

    public AbstractPageList getPageList() {
        return this.pageList;
    }

    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    public AbstractTitleHolder getReadyTitleHolder() {
        return this.loadedTitleHolder;
    }

    public List<AbstractValueHolder> getReadyHolders() {
        return this.loadedTeamHolders;
    }

    public void loadTeamHolders() throws BoardException {
        List<AbstractValueHolder> holders = null;
        try {
            holders = createTeamHolders();
        }
        catch (Exception e) {
            throw new BoardException(e.getMessage());
        }

        if (holders == null) {
            throw new BoardException("Holders list can not be null!");
        }

        int size = holders.size();

        if (size < 1) {
            throw new BoardException("At least 1 holder requited!");
        }

        if (size > BoardManager.MAX_ENTRY_SIZE) {
            throw new BoardException("Exceded maximum elements size " + BoardManager.MAX_ENTRY_SIZE + " (" + holders.size() + ")");
        }

        this.loadedTeamHolders = Collections.unmodifiableList(holders);
    }

    public void loadTitleHolder() throws BoardException {
        AbstractTitleHolder holder;
        try {
            holder = createTitleHolder();
        }
        catch (Exception e) {
            throw new BoardException(e.getMessage());
        }

        if (holder == null) {
            throw new BoardException("Title holder can not be null!");
        }

        this.loadedTitleHolder = holder;
    }

    public boolean isVisibleToPlayer() {
        try {
            return isVisible();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getTimeToChangePage() {
        try {
            return getTimeToChange();
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public ScoreSequenceFactory getScoreSequence() {
        ScoreSequenceFactory scoreSequenceFactory;
        try {
            scoreSequenceFactory = getScoreSequenceFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
            scoreSequenceFactory = DefaultScoreSequenceFactory.TO_ZERO;
        }

        if (scoreSequenceFactory == null) {
            scoreSequenceFactory = DefaultScoreSequenceFactory.TO_ZERO;
        }

        return scoreSequenceFactory;
    }

    protected abstract ScoreSequenceFactory getScoreSequenceFactory();

    protected abstract long getTimeToChange();

    protected abstract boolean isVisible();

    protected abstract AbstractTitleHolder createTitleHolder();

    protected abstract List<AbstractValueHolder> createTeamHolders();
}
