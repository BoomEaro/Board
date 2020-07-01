package ru.boomearo.board.objects.hooks;

import ru.boomearo.adveco.AdvEco;

public class AdvEcoPlugin implements IHookPlugin {

	private AdvEco eco;
	
	public AdvEcoPlugin(AdvEco eco) {
		this.eco = eco;
	}
	
	@Override
	public AdvEco getPlugin() {
		return this.eco;
	}

}
