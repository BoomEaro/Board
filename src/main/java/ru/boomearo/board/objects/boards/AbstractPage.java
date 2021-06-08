package ru.boomearo.board.objects.boards;

import ru.boomearo.board.exceptions.BoardException;
import ru.boomearo.board.managers.BoardManager;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPage {

    private final AbstractPageList pageList;

    private List<AbstractHolder> loadedHolders = Collections.emptyList();

    public AbstractPage(AbstractPageList pageList) {
        this.pageList = pageList;
    }

    public AbstractPageList getPageList() {
        return this.pageList;
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
        if (size > BoardManager.maxEntrySize) {
            throw new BoardException("Количество холдеров превышает максимальное количество в " + BoardManager.maxEntrySize + " (" + holders.size() + ")");
        }

        this.loadedHolders = Collections.unmodifiableList(holders);
    }

    //Обрабатываем возможные ошибки при попытке узнать видимость страницы
    public boolean isVisibleToPlayer() {
        boolean visible = false;
        try {
            visible = isVisible();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return visible;
    }

    //Учитываем возможные ошибки а так же проверяем на null
    public String getBoardTitle() {
        String title = "NoTitle";
        try {
            String tmp = getTitle();
            if (tmp != null) {
                title = tmp;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return title;
    }

    //Учитываем возможные ошибки
    public int getTimeToChangePage() {
        int time = 1;
        try {
            time = getTimeToChange();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    //Абстракции
    protected abstract int getTimeToChange();
    protected abstract boolean isVisible();
    protected abstract String getTitle();
    protected abstract List<AbstractHolder> createHolders();
}
