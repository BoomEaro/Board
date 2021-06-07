package ru.boomearo.board.objects.boards.arcade.pages;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.utils.DateUtil;
import ru.boomearo.cities.Cities;
import ru.boomearo.cities.objects.AdminCity;
import ru.boomearo.cities.objects.City;
import ru.boomearo.cities.objects.CityVillager;
import ru.boomearo.cities.objects.regions.RentRegion;
import ru.boomearo.nations.objects.NationType;

public class ArcadeCityPage extends AbstractPage {

    public ArcadeCityPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return 15;
    }


    @Override
    public boolean isVisible() {
        return Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName()) != null;
    }

    @Override
    public String getTitle() {
        return "§6§lГород";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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
                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
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

                CityVillager cv = Cities.getInstance().getCityManager().getCityVillager(getPageList().getPlayerBoard().getPlayer().getName());
                if (cv != null) {
                    City city = cv.getCity();
                    if (city != null) {
                        RentRegion rr = cv.getRegion();
                        if (rr != null) {
                            if (cv.isIgnoreRent()) {
                                return "§bИммунитет на выселение";
                            }
                            return "§c" + DateUtil.formatedTimeSimple(((rr.getRentTime() - System.currentTimeMillis()) / 1000), false);
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

}
