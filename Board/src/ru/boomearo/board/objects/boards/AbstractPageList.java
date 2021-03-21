package ru.boomearo.board.objects.boards;

import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.objects.PlayerBoard;

public abstract class AbstractPageList {

    private final PlayerBoard player;
    
    private List<AbstractPage> pages = new ArrayList<AbstractPage>();
    
    public AbstractPageList(PlayerBoard player) {
        this.player = player;
    }
    
    public PlayerBoard getPlayerBoard() {
        return this.player;
    }
    
    //Возвращает актуальное состояние страниц для этого игрока
    public List<AbstractPage> getPages() {
        return this.pages;
    }
    
    //Инициализирует страницы, то какие страницы были созданы, зависит от реализации этого класса
    public void loadPages() {
        this.pages = createPages();
        
        //После инициализации, инициализируем холдеры
        for (AbstractPage ap : this.pages) {
            ap.loadHolders();
        }
    }
    
    protected abstract List<AbstractPage> createPages();
}
