package ru.boomearo.board.objects;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerBoard {

    UUID getUuid();

    Player getPlayer();

    Plugin getPlugin();

    BoardManager getBoardManager();

    int getPageIndex();

    void setPageIndex(int pageIndex);

    long getPageCreateTime();

    void setPageCreateTime(long pageCreateTime);

    boolean isPermanentView();

    void setPermanentView(boolean permanentView);

    boolean isDebugMode();

    void setDebugMode(boolean debugMode);

    boolean isInit();

    void setNewPageList(AbstractPageList pageList);

    CompletableFuture<AbstractPage> getCurrentPage();

    CompletableFuture<AbstractPage> getPageByIndex(int index);

    CompletableFuture<Integer> getMaxPageIndex();

    CompletableFuture<Integer> getNextPageNumber();

    void toPage(int indexTo, AbstractPage toPage);

    void submitUpdate();

    void remove();
}
