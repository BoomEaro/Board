package ru.boomearo.board.objects.boards;

import lombok.Getter;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Collections;
import java.util.List;

@Getter
public abstract class AbstractPage {

    protected final AbstractPageList pageList;
    protected final PlayerBoard playerBoard;

    private AbstractTitleHolder loadedTitleHolder;
    private List<AbstractValueHolder> loadedTeamHolders;

    public AbstractPage(AbstractPageList pageList) {
        this.pageList = pageList;
        this.playerBoard = this.pageList.playerBoard;
    }

    public AbstractTitleHolder getReadyTitleHolder() {
        return this.loadedTitleHolder;
    }

    public List<AbstractValueHolder> getReadyHolders() {
        return this.loadedTeamHolders;
    }

    public void loadTeamHolders() {
        List<AbstractValueHolder> holders = createTeamHolders();

        if (holders == null) {
            throw new IllegalStateException("Holders list can not be null!");
        }

        int size = holders.size();

        if (size < 1) {
            throw new IllegalStateException("At least 1 holder requited!");
        }

        if (size > BoardManager.MAX_ENTRY_SIZE) {
            throw new IllegalStateException("Exceded maximum elements size " + BoardManager.MAX_ENTRY_SIZE + " (" + holders.size() + ")");
        }

        this.loadedTeamHolders = Collections.unmodifiableList(holders);
    }

    public void loadTitleHolder() {
        AbstractTitleHolder holder = createTitleHolder();

        if (holder == null) {
            throw new IllegalStateException("Title holder can not be null!");
        }

        this.loadedTitleHolder = holder;
    }

    public boolean isVisibleToPlayer() {
        try {
            return isVisible();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getTimeToChangePage() {
        try {
            return getTimeToChange();
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public ScoreSequenceFactory getScoreSequence() {
        ScoreSequenceFactory scoreSequenceFactory;
        try {
            scoreSequenceFactory = getScoreSequenceFactory();
        } catch (Exception e) {
            e.printStackTrace();
            scoreSequenceFactory = DefaultScoreSequenceFactory.TO_ZERO;
        }

        if (scoreSequenceFactory == null) {
            scoreSequenceFactory = DefaultScoreSequenceFactory.TO_ZERO;
        }

        return scoreSequenceFactory;
    }

    protected ScoreSequenceFactory getScoreSequenceFactory() {
        return DefaultScoreSequenceFactory.TO_ZERO;
    }

    public void performUpdate() {
        try {
            onUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onUpdate() {

    }

    protected abstract long getTimeToChange();

    protected abstract boolean isVisible();

    protected abstract AbstractTitleHolder createTitleHolder();

    protected abstract List<AbstractValueHolder> createTeamHolders();
}
