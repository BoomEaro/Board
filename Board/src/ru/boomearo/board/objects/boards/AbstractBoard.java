package ru.boomearo.board.objects.boards;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.runnable.AbstractTimer;

public abstract class AbstractBoard extends AbstractTimer {

    //Если будет работать плохо из-за асинхронного обновления скорборда, то сделаем в основном потоке..
	public AbstractBoard(String name, int time) {
	    super(name, TimeUnit.SECONDS, time);
	}
	
	@Override
	public void task() {
		update();
	}
	
	public void update() {
		try {
			for (PlayerBoard pb : Board.getInstance().getBoardManager().getAllPlayerBoards()) {
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
					
					if (pb.getUpdatePageCount() >= thisPage.getTimeToChange()) {
						
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
	
	public abstract List<AbstractPage> getPages(PlayerBoard player);

}
