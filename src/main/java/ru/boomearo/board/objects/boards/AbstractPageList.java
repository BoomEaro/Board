package ru.boomearo.board.objects.boards;

import java.util.Collections;
import java.util.List;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.objects.PlayerBoard;

public abstract class AbstractPageList {

    private final PlayerBoard player;

    private List<AbstractPage> pages = Collections.emptyList();

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
    public void loadPages() throws BoardException {
        List<AbstractPage> tmp = null;
        try {
            tmp = createPages();
        }
        catch (Exception e) {
            throw new BoardException(e.getMessage());
        }

        if (tmp == null) {
            throw new BoardException("Список страниц не должен быть нулевым!");
        }

        if (tmp.isEmpty()) {
            throw new BoardException("Список страниц пустой!");
        }

        //Инициализируем каждую страницу
        //TODO стоит пропускать исключения? loadHolders может вызвать просто это
        for (AbstractPage ap : tmp) {
            ap.loadHolders();
        }

        this.pages = Collections.unmodifiableList(tmp);
    }

    protected abstract List<AbstractPage> createPages();
}
