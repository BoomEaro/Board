package ru.boomearo.board.objects.boards;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;

public class ExpBoard extends AbstractBoard {

	public ExpBoard() {
        super("ExpBoard", 1);
    }

	@Override
	public List<AbstractPage> getPages(PlayerBoard player) {
		
		List<AbstractPage> tmpBoards = new ArrayList<AbstractPage>();
		
		tmpBoards.add(new AbstractPage(player) {
			
			@Override
			public int getTimeToChange() {
				return 1;
			}

			@Override
			public String getTitle() {
				return "§cПустой борд";
			}
			
			@Override
			public List<AbstractHolder> createHolders() {
				List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						return this.getPage().getPlayerBoard().getPlayer().getDisplayName();
					}
					
				});
				return tmpLines;
			}

			@Override
			public boolean isVisible() {
				return true;
			}

		});
		return tmpBoards;
	}

}
