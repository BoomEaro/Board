package ru.boomearo.board.objects.boards;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.PlayerBoard;

public abstract class AbstractBoard extends BukkitRunnable {
	
	private final int updateTime;
	
	public AbstractBoard() {
		this.updateTime = updateTime();
		update();
		runnable();
	}
	
	private void runnable() {
		this.runTaskTimer(Board.getContext(), this.updateTime, this.updateTime);
	}
	
	@Override
	public void run() {
		update();
	}
	
	public void update() {
		try {
			for (PlayerBoard pb : Board.getContext().getBoardManager().getAllPlayerBoards()) {
				int maxPage = pb.getMaxPageIndex();
				if (pb.getPageIndex() <= maxPage) {
					AbstractPage thisPage = pb.getCurrentPage();
					
					int nextPageIndex = pb.getNextPageNumber();
					AbstractPage nextPage = pb.getPageByIndex(nextPageIndex);
					
					boolean isVisible = thisPage.isVisible();
					
					
					if (!isVisible) {
						
						if (pb.getPageIndex() != nextPageIndex) {
							pb.toPage(nextPageIndex, nextPage);
						}
						
						continue;
					}
					
					if (pb.getUpdatePageCount() >= (thisPage.getTimeToChange() / this.updateTime)) {
						
						//Board.getContext().getLogger().info(pb.getPlayer().getDisplayName() + " -> " + nextPageIndex + " " + pb.getUpdatePageCount() + " " + (thisPage.getTimeToChange() / this.updateTime) + " " + (pb.getUpdatePageCount() >= (thisPage.getTimeToChange() / this.updateTime)) + " " + (pb.getPageIndex() != nextPageIndex) + " " + !thisPage.isVisible() + " " + !pb.isPermanentView());
						if (pb.getPageIndex() != nextPageIndex) {
							if (isVisible) {
								if (pb.isPermanentView()) {
									pb.update();
									continue;
								}
							}
							
							
							pb.toPage(nextPageIndex, nextPage);
							
							pb.update();
							continue;
						}
					}
					pb.update();
				}
				pb.addUpdatePageCount(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract int updateTime();
	
	public abstract List<AbstractPage> getPages(PlayerBoard player);
	
	public int getReadyTime() {
		return this.updateTime;
	}
}
