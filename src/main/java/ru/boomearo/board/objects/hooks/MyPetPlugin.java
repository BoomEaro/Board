package ru.boomearo.board.objects.hooks;

public class MyPetPlugin implements IHookPlugin {

	private final de.Keyle.MyPet.MyPetPlugin pet;
	
	public MyPetPlugin(de.Keyle.MyPet.MyPetPlugin pet) {
		this.pet = pet;
	}
	
	@Override
	public de.Keyle.MyPet.MyPetPlugin getPlugin() {
		return this.pet;
	}

}
