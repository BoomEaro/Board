package ru.boomearo.board.objects.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class HookManager {

    private CitiesPlugin cities = null;
    private AdvEcoPlugin adveco = null;
    private MyPetPlugin mypet = null;
    private NationsPlugin nations = null;
    private QuestsPlugin quests = null;

    public HookManager() {
        checkSoft();
    }

    public void checkSoft() {
        try {

            Plugin city = Bukkit.getPluginManager().getPlugin("Cities");
            if (city != null) {

                this.cities = new CitiesPlugin((ru.boomearo.cities.Cities) city);
            }

            Plugin eco = Bukkit.getPluginManager().getPlugin("AdvEco");
            if (eco != null) {

                this.adveco = new AdvEcoPlugin((ru.boomearo.adveco.AdvEco) eco);
            }

            Plugin pet = Bukkit.getPluginManager().getPlugin("MyPet");
            if (pet != null) {

                this.mypet = new MyPetPlugin((de.Keyle.MyPet.MyPetPlugin) pet);
            }

            Plugin nat = Bukkit.getPluginManager().getPlugin("Nations");
            if (nat != null) {

                this.nations = new NationsPlugin((ru.boomearo.nations.Nations) nat);
            }

            Plugin que = Bukkit.getPluginManager().getPlugin("Quests");
            if (que != null) {

                this.quests = new QuestsPlugin((ru.boomearo.quests.Quests) que);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CitiesPlugin getCities() {
        return this.cities;
    }
    public AdvEcoPlugin getAdvEco() {
        return this.adveco;
    }
    public MyPetPlugin getMyPet() {
        return this.mypet;
    }
    public NationsPlugin getNations() {
        return this.nations;
    }
    public QuestsPlugin getQuests() {
        return this.quests;
    }
}
