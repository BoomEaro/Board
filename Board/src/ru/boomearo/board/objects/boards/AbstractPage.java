package ru.boomearo.board.objects.boards;

import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;

public abstract class AbstractPage {
	
	private PlayerBoard player;
	
	private List<AbstractHolder> loadedHolders;
	
	public AbstractPage(PlayerBoard player) {
		this.player = player;
		
		this.loadedHolders = createHolders();
	}
	
	public PlayerBoard getPlayerBoard() {
		return this.player;
	}
	
	public List<AbstractHolder> getReadyHolders() {
		return this.loadedHolders;
	}
	
	public abstract int getTimeToChange();
	
	public abstract boolean isVisible();
	
	public abstract String getTitle();
	
	protected abstract List<AbstractHolder> createHolders();
}
