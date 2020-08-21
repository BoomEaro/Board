package ru.boomearo.board.objects.boards;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.MyPet.PetState;
import de.Keyle.MyPet.api.util.locale.Translation;
import ru.boomearo.adveco.AdvEco;
import ru.boomearo.adveco.exceptions.EcoException;
import ru.boomearo.adveco.managers.EcoManager;
import ru.boomearo.adveco.objects.EcoType;
import ru.boomearo.board.Board;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.utils.DateUtil;
import ru.boomearo.cities.Cities;
import ru.boomearo.cities.objects.AdminCity;
import ru.boomearo.cities.objects.City;
import ru.boomearo.cities.objects.CityVillager;
import ru.boomearo.cities.objects.regions.RentRegion;
import ru.boomearo.nations.Nations;
import ru.boomearo.nations.objects.NationType;
import ru.boomearo.nations.objects.PlayerNation;

public class ServerBoard extends AbstractBoard {

    public ServerBoard() {
        super("ServerBoard", 1);
    }

    @Override
    public List<AbstractPage> getPages(PlayerBoard player) {

        List<AbstractPage> tmpBoards = new ArrayList<AbstractPage>();
        tmpBoards.add(new AbstractPage(player) {

            @Override
            public int getTimeToChange() {
                return 20;
            }

            @Override
            public String getTitle() {
                return "§lPlugin§a§lWorld";
            }

            @Override
            public List<AbstractHolder> createHolders() {
                List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();

                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§7Добро пожаловать!";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return this.getPage().getPlayerBoard().getPlayer().getDisplayName();
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        PlayerNation pn = Nations.getInstance().getNationsManager().getLoadedPlayerNation(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (pn != null) {
                            NationType type = pn.getNation();
                            return "§7Раса: " + type.getColor() + type.getName();
                        }
                        return "§7Раса: §cОтсутствует.";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        EcoManager eco = AdvEco.getInstance().getEcoManager();
                        Player pl = this.getPage().getPlayerBoard().getPlayer();
                        try {
                            return "§7Кредиты: §a" + EcoManager.getFormatedEco(eco.getPlayerEco(pl.getName(), pl, EcoType.Credit), EcoType.Credit);
                        } 
                        catch (EcoException e) {
                            return "";
                        }
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        EcoManager eco = AdvEco.getInstance().getEcoManager();
                        Player pl = this.getPage().getPlayerBoard().getPlayer();
                        try {
                            return "§7Медь: §a" + EcoManager.getFormatedEco(eco.getPlayerEco(pl.getName(), pl, EcoType.Copper), EcoType.Copper);
                        } 
                        catch (EcoException e) {
                            return "";
                        }
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        EcoManager eco = AdvEco.getInstance().getEcoManager();
                        Player pl = this.getPage().getPlayerBoard().getPlayer();
                        try {
                            return "§7Кристаллов: §b" + EcoManager.getFormatedEco(eco.getPlayerEco(pl.getName(), pl, EcoType.Crystal), EcoType.Crystal);
                        } 
                        catch (EcoException e) {
                            return "";
                        }
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§7Статистика убийств:";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§c" + this.getPage().getPlayerBoard().getPlayer().getStatistic(Statistic.PLAYER_KILLS) + " §7игроков";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§c" + this.getPage().getPlayerBoard().getPlayer().getStatistic(Statistic.MOB_KILLS) + " §7мобов";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§c" + this.getPage().getPlayerBoard().getPlayer().getStatistic(Statistic.DEATHS) + " §7смертей";
                    }

                });
                return tmpLines;
            }

            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tmpBoards.add(new AbstractPage(player) {

            @Override
            public int getTimeToChange() {
                return 15;
            }

            @Override
            public String getTitle() {
                return "§a§lПитомец";
            }

            @Override
            public List<AbstractHolder> createHolders() {
                List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        MyPet mp = MyPetApi.getMyPetManager().getMyPet(this.getPage().getPlayerBoard().getPlayer());

                        if (mp != null) {
                            return "§7" + mp.getPetName() + (mp.getSkilltree() != null ? " §7[§6" + mp.getSkilltree().getDisplayName() + "§7]" : "");
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        MyPet mp = MyPetApi.getMyPetManager().getMyPet(this.getPage().getPlayerBoard().getPlayer());

                        if (mp != null) {
                            int level = mp.getExperience().getLevel();
                            return "§7Уровень: §b" + level;
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        MyPet mp = MyPetApi.getMyPetManager().getMyPet(this.getPage().getPlayerBoard().getPlayer());

                        if (mp != null) {
                            return "§7Сытость: §6" + df.format(mp.getSaturation()) + "§7%";
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        MyPet mp = MyPetApi.getMyPetManager().getMyPet(this.getPage().getPlayerBoard().getPlayer());

                        if (mp != null) {
                            return "§7Здоровье: §c" + df.format(mp.getHealth()) + "§7/§c" + df.format(mp.getMaxHealth());
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        MyPet mp = MyPetApi.getMyPetManager().getMyPet(this.getPage().getPlayerBoard().getPlayer());

                        if (mp != null) {
                            return "§7Сущность: §a" + Translation.getString("Name." + mp.getPetType().name(), mp.getOwner().getLanguage());
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        MyPet mp = MyPetApi.getMyPetManager().getMyPet(this.getPage().getPlayerBoard().getPlayer());

                        if (mp != null) {
                            return "§7Статус: §6" + getPetStatus(mp.getStatus());
                        }
                        return " ";
                    }

                });
                return tmpLines;
            }

            @Override
            public boolean isVisible() {
                return MyPetApi.getMyPetManager().getMyPet(this.getPlayerBoard().getPlayer()) != null;
            }
        });
        tmpBoards.add(new AbstractPage(player) {

            @Override
            public int getTimeToChange() {
                return 15;
            }


            @Override
            public String getTitle() {
                return "§6§lГород";
            }

            @Override
            public List<AbstractHolder> createHolders() {
                List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            if (city != null) {
                                return "§7Тег " + city.getValidDisplayName();
                            }
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            if (city != null) {
                                NationType type = city.getNation();
                                return "§7Раса: " + type.getColor() + type.getName() + (city.isCapital() ? " §8(" + City.capitalSymbol + "§8)" : "");
                            }
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            if (city != null) {
                                CityVillager mayor = city.getMayor();
                                return "§7Мэр: §c" + (mayor != null ? mayor.getName() : "Отсутствует.");
                            }
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            if (city != null) {
                                CityVillager submayor = city.getSubMayor();
                                return "§7Заместитель: §4" + (submayor != null ? submayor.getName() : "Отсутствует.");
                            }
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            boolean adminCity = (city instanceof AdminCity);
                            return "§7Население: §6" + city.getAllCityVillager().size() + "§7/§6" + (adminCity ? "∞" : city.getAllowedPlots());
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            boolean adminCity = (city instanceof AdminCity);
                            return "§7Аренд: §6" + city.getAllRegions().size() + "§7/§c" + (adminCity ? "∞" : city.getAllowedPlots());
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();

                            return "§7Используется: §6" + city.getRentBusyAmount() + "§7/§c" + city.getAllRegions().size();
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();

                            return "§7Общее развитие: §6" + city.getCurrentEvolute();
                        }
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§7До выселения:";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(this.getPage().getPlayerBoard().getPlayer().getName());
                        if (cv != null) {
                            City city = cv.getCity();
                            if (city != null) {
                                RentRegion rr = cv.getRegion();
                                if (rr != null) {
                                    if (cv.isIgnoreRent()) {
                                        return "§bИммунитет на выселение";
                                    }
                                    return "§c" + DateUtil.formatedTimeSimple(city.getRentMaxTime() - ((System.currentTimeMillis() - rr.getRentTime()) / 1000), false);
                                }
                                else {
                                    return "§cОтсутствует аренда";
                                }
                            }
                        }
                        return " ";
                    }

                });
                return tmpLines;
            }

            @Override
            public boolean isVisible() {
                return Cities.getInstance().getCityManager().getCityVillager(this.getPlayerBoard().getPlayer().getName()) != null;
            }
        });
        tmpBoards.add(new AbstractPage(player) {

            @Override
            public int getTimeToChange() {
                return 60;
            }


            @Override
            public String getTitle() {
                return "§c§lСервер";
            }

            @Override
            public List<AbstractHolder> createHolders() {
                List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§6Нагрузка на сервер:";
                    }


                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        double tps = Board.getInstance().getTpsRunnable().getTps();

                        String forTps = getTpsFormat(tps);

                        return "§6TPS: " + forTps;

                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§6Использование памяти:";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {

                        return getMemoryPrecentColor();
                    }

                    @Override
                    public long getMaxCacheTime() {
                        return 200;
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return " ";
                    }

                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§6Сущностей: §c" + Board.getInstance().getTpsRunnable().getEntites();
                    }
                });
                tmpLines.add(new AbstractHolder(this) {

                    @Override
                    public String getText() {
                        return "§6Чанков: §c" + Board.getInstance().getTpsRunnable().getChunks();
                    }

                });
                return tmpLines;
            }

            @Override
            public boolean isVisible() {
                return this.getPlayerBoard().isDebugMode();
            }
        });
        return tmpBoards;
    }

    private String getFormatMemory(String colorFirst, String colorSecond) {
        double maxMemory = (Runtime.getRuntime().totalMemory() / 1024d / 1024d);
        double currentMemory = (maxMemory - (Runtime.getRuntime().freeMemory() / 1024d / 1024d));

        double i1 = maxMemory / 23.0D;
        double proc = currentMemory / i1;
        StringBuffer sb = new StringBuffer("▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎");

        sb.insert((int)proc, colorFirst + "▎§0");
        return colorSecond + sb.toString();
    }

    private String getMemoryPrecentColor() {
        double maxMemory = (Runtime.getRuntime().totalMemory() / 1024d / 1024d);
        double currentMemory = (maxMemory - (Runtime.getRuntime().freeMemory() / 1024d / 1024d));

        double i1 = maxMemory / 100.0D;
        double proc = currentMemory / i1;

        if (proc <= 10) {
            return getFormatMemory("§f", "§f");
        }
        if (proc <= 25) {
            return getFormatMemory("§7", "§f");
        }
        if (proc <= 50) {
            return getFormatMemory("§2", "§a");
        }
        if (proc <= 75) {
            return getFormatMemory("§e", "§6");
        }
        if (proc <= 90) {
            return getFormatMemory("§e", "§6");
        }
        if (proc <= 100) {
            return getFormatMemory("§4", "§4");
        }
        return getFormatMemory("§7", "§f");
    }

    private String getTpsFormat(double tps) {
        if (tps > 20) {
            tps = 20;
        }
        if (tps <= 5) {
            return "§4" + df.format(tps) + " (ужасная)";
        }
        if (tps <= 10) {
            return "§4" + df.format(tps) + " (гиганская)";
        }
        if (tps <= 15) {
            return "§4" + df.format(tps) + " (большая)";
        }
        if (tps <= 16) {
            return "§c" + df.format(tps) + " (высокая)";
        }
        if (tps <= 17) {
            return "§e" + df.format(tps) + " (средняя)";
        }
        if (tps <= 18) {
            return "§6" + df.format(tps) + " (легкая)";
        }
        if (tps <= 19) {
            return "§2" + df.format(tps) + " (небольшая)";
        }
        if (tps <= 20) {
            return "§a" + df.format(tps) + " (отсутствует)";
        }
        return "§a*" + df.format(tps);
    }

    private String getPetStatus(PetState state) {
        switch (state) {
            case Here: return "Здесь";
            case Dead: return "Мёртв";
            case Despawned: return "Скрыт";
            case PetState: return state.name();
            default: return "unknown " + state.name();
        }
    }

    private static final DecimalFormat df = new DecimalFormat("#.##");

}
