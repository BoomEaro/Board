package ru.boomearo.board.objects.boards;

import java.util.List;

public abstract class AbstractPage {
    
    private final AbstractPageList pageList;
    
	private final List<AbstractHolder> loadedHolders;
	
	public AbstractPage(AbstractPageList pageList) {
	    this.pageList = pageList;
		this.loadedHolders = createHolders();
	}
	
	public AbstractPageList getPageList() {
	    return this.pageList;
	}
	
	public List<AbstractHolder> getReadyHolders() {
		return this.loadedHolders;
	}
	
	public abstract int getTimeToChange();
	
	public abstract boolean isVisible();
	
	public abstract String getTitle();
	
	protected abstract List<AbstractHolder> createHolders();
}
