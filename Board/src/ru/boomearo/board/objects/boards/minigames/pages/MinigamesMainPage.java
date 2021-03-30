package ru.boomearo.board.objects.boards.minigames.pages;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class MinigamesMainPage extends AbstractPage {

    public MinigamesMainPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getTitle() {
        return "§f§lPlugin§a§lWorld";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> holders = new ArrayList<AbstractHolder>();
        
        holders.add(new AbstractHolder(this) {

            @Override
            protected String getText() {
                return "§7Добро пожаловать!";
            }
            
        });
        
        holders.add(new AbstractHolder(this) {

            @Override
            protected String getText() {
                return getPageList().getPlayerBoard().getPlayer().getDisplayName();
            }
            
        });
        
        holders.add(new AbstractHolder(this) {

            @Override
            protected String getText() {
                return " ";
            }
            
        });
        
        holders.add(new AbstractHolder(this) {

            @Override
            protected String getText() {
                String name = getPageList().getPlayerBoard().getPlayer().getName();
                double money = Vault.getMoney(name);
                return "§7Жетоны: " + GameControl.getFormatedEco(money);
            }
            
        });
        
        
        return holders;
    }

    
}
