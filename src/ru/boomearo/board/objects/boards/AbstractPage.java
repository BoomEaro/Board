package ru.boomearo.board.objects.boards;

import java.util.List;

public abstract class AbstractPage {

    private final AbstractPageList pageList;

    private List<AbstractHolder> loadedHolders = null;

    public AbstractPage(AbstractPageList pageList) {
        this.pageList = pageList;
    }

    public AbstractPageList getPageList() {
        return this.pageList;
    }

    public List<AbstractHolder> getReadyHolders() {
        return this.loadedHolders;
    }

    public void loadHolders() {
        this.loadedHolders = createHolders();
    }

    public abstract int getTimeToChange();

    public abstract boolean isVisible();

    public abstract String getTitle();

    protected abstract List<AbstractHolder> createHolders();
}
