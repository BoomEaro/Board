package ru.boomearo.board.objects.hooks;

import ru.boomearo.nations.Nations;

public class NationsPlugin implements IHookPlugin {

	private Nations nation;
	
	public NationsPlugin(Nations nation) {
		this.nation = nation;
	}
	
	@Override
	public Nations getPlugin() {
		return this.nation;
	}

}
