package ru.boomearo.board.objects.boards;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPage {

    protected final AbstractPageList pageList;
    protected final PlayerBoard playerBoard;

    private List<AbstractHolder> loadedHolders = Collections.emptyList();

    public AbstractPage(AbstractPageList pageList) {
        this.pageList = pageList;
        this.playerBoard = this.pageList.playerBoard;
    }

    public AbstractPageList getPageList() {
        return this.pageList;
    }

    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    public List<AbstractHolder> getReadyHolders() {
        return this.loadedHolders;
    }

    //Проверяем список холдеров на исключения и размеры при загрузке.
    public void loadHolders() throws BoardException {
        List<AbstractHolder> holders = null;
        try {
            holders = createHolders();
        }
        catch (Exception e) {
            throw new BoardException(e.getMessage());
        }

        if (holders == null) {
            throw new BoardException("Список холдеров нулевой!");
        }

        int size = holders.size();

        if (size < 1) {
            throw new BoardException("Минимальное количество холдеров должно быть 1!");
        }

        //TODO может быть обрезать их и не вызывать исключение?
        if (size > BoardManager.MAX_ENTRY_SIZE) {
            throw new BoardException("Количество холдеров превышает максимальное количество в " + BoardManager.MAX_ENTRY_SIZE + " (" + holders.size() + ")");
        }

        this.loadedHolders = Collections.unmodifiableList(holders);
    }

    //Обрабатываем возможные ошибки при попытке узнать видимость страницы
    public boolean isVisibleToPlayer() {
        try {
            return isVisible();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Учитываем возможные ошибки, а так же проверяем на null
    public String getBoardTitle() {
        String title;
        try {
            title = getTitle();
        }
        catch (Exception e) {
            title = "error";
            e.printStackTrace();
        }

        if (title == null) {
            title = "empty";
        }

        return title;
    }

    //Учитываем возможные ошибки
    public int getTimeToChangePage() {
        try {
            return getTimeToChange();
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    //Абстракции
    protected abstract int getTimeToChange();

    protected abstract boolean isVisible();

    protected abstract String getTitle();

    protected abstract List<AbstractHolder> createHolders();
}
