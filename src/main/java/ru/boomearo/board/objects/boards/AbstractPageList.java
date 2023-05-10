package ru.boomearo.board.objects.boards;

import java.util.Collections;
import java.util.List;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.objects.PlayerBoard;

public abstract class AbstractPageList {

    protected final PlayerBoard playerBoard;

    private List<AbstractPage> pages = Collections.emptyList();

    public AbstractPageList(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    //Возвращает актуальное состояние страниц для этого игрока
    public List<AbstractPage> getPages() {
        return this.pages;
    }

    //Инициализирует страницы, то какие страницы были созданы, зависит от реализации этого класса
    public void loadPages() throws BoardException {
        List<AbstractPage> tmp;
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
            ap.loadTitleHolder();
            ap.loadTeamHolders();
        }

        this.pages = Collections.unmodifiableList(tmp);
    }

    protected abstract List<AbstractPage> createPages();
}
