package ru.boomearo.board.objects.hooks;

import ru.boomearo.quests.Quests;

public class QuestsPlugin implements IHookPlugin {

	private final Quests quests;
	
	public QuestsPlugin(Quests quests) {
		this.quests = quests;
	}
	
	@Override
	public Quests getPlugin() {
		return this.quests;
	}

}
