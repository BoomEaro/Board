package ru.boomearo.board.objects.hooks;

import ru.boomearo.cities.Cities;

public class CitiesPlugin implements IHookPlugin {

	private final Cities city;
	
	public CitiesPlugin(Cities city) {
		this.city = city;
	}
	
	@Override
	public Cities getPlugin() {
		return city;
	}

}
