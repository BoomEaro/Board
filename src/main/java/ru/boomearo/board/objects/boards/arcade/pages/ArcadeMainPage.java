package ru.boomearo.board.objects.boards.arcade.pages;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import ru.boomearo.adveco.AdvEco;
import ru.boomearo.adveco.exceptions.EcoException;
import ru.boomearo.adveco.managers.EcoManager;
import ru.boomearo.adveco.objects.EcoType;
import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.nations.Nations;
import ru.boomearo.nations.objects.NationType;
import ru.boomearo.nations.objects.PlayerNation;

public class ArcadeMainPage extends AbstractPage {

    public ArcadeMainPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return 20;
    }
    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getTitle() {
        return "§lPlugin§a§lWorld";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
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
                return getPageList().getPlayerBoard().getPlayer().getDisplayName();
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
                PlayerNation pn = Nations.getInstance().getNationsManager().getLoadedPlayerNation(getPageList().getPlayerBoard().getPlayer().getName());
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
                Player pl = getPageList().getPlayerBoard().getPlayer();
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
                Player pl = getPageList().getPlayerBoard().getPlayer();
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
                Player pl = getPageList().getPlayerBoard().getPlayer();
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
                return "§c" + getPageList().getPlayerBoard().getPlayer().getStatistic(Statistic.PLAYER_KILLS) + " §7игроков";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§c" + getPageList().getPlayerBoard().getPlayer().getStatistic(Statistic.MOB_KILLS) + " §7мобов";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§c" + getPageList().getPlayerBoard().getPlayer().getStatistic(Statistic.DEATHS) + " §7смертей";
            }

        });
        return tmpLines;
    }

}
