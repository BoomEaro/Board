package ru.boomearo.board.objects.boards.test.pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;
import ru.boomearo.board.utils.DateUtil;

public class TestMainPage extends AbstractPage {

    private static final DecimalFormat df = new DecimalFormat("#.##");
    
    public TestMainPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return 10;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
    @Override
    public String getTitle() {
        return "§c§lТестовый сервер";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§cТекущие координаты:";
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§fX: §c" + df.format(getPageList().getPlayerBoard().getPlayer().getLocation().getX());
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 100;
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§fY: §c" + df.format(getPageList().getPlayerBoard().getPlayer().getLocation().getY());
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 100;
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§fZ: §c" + df.format(getPageList().getPlayerBoard().getPlayer().getLocation().getZ());
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 100;
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                Location loc = getPageList().getPlayerBoard().getPlayer().getLocation();
                return "§fМир: §c" + loc.getWorld().getName();
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 100;
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
                return "§6Аптайм:";
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§c" + DateUtil.formatedTimeSimple(System.currentTimeMillis() - Board.uptime, true);
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 100;
            }
        });
        return tmpLines;
    }

}
