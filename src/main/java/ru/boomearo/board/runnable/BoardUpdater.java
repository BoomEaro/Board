package ru.boomearo.board.runnable;

import java.util.concurrent.TimeUnit;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;

public class BoardUpdater extends AbstractTimer {

    //Если будет работать плохо из-за асинхронного обновления скорборда, то сделаем в основном потоке..
    public BoardUpdater() {
        super("BoardUpdater", TimeUnit.SECONDS, 1);
    }

    @Override
    public void task() {
        update();
    }

    public void update() {
        try {
            //Получаем все скорборды игроков
            for (PlayerBoard pb : Board.getInstance().getBoardManager().getAllPlayerBoards()) {
                int maxPage = pb.getMaxPageIndex();
                if (pb.getPageIndex() <= maxPage) {
                    AbstractPage thisPage = pb.getCurrentPage();

                    int nextPageIndex = pb.getNextPageNumber();
                    AbstractPage nextPage = pb.getPageByIndex(nextPageIndex);

                    //Если текущая страница не видна игроку
                    if (!thisPage.isVisibleToPlayer()) {

                        //Убеждаемся что текущая страница не является следующей страницей (в противном случае ничего не делаем)
                        if (pb.getPageIndex() != nextPageIndex) {
                            pb.toPage(nextPageIndex, nextPage);
                        }

                        continue;
                    }

                    //Сменяем страницу только если прошло время, иначе просто обновляем ее
                    if (pb.getUpdatePageCount() >= thisPage.getTimeToChangePage()) {

                        //Убеждаемся что текущая страница не является следующей
                        //Board.getInstance().getLogger().info(pb.getPlayer().getDisplayName() + " -> " + nextPageIndex + " " + pb.getUpdatePageCount() + " " + (thisPage.getTimeToChange() / this.updateTime) + " " + (pb.getUpdatePageCount() >= (thisPage.getTimeToChange() / this.updateTime)) + " " + (pb.getPageIndex() != nextPageIndex) + " " + !thisPage.isVisible() + " " + !pb.isPermanentView());
                        if (pb.getPageIndex() != nextPageIndex) {
                            //Если оказывается что в настройках игрока отключен авто скролл, то просто обновляем страницу.
                            //Иначе пытаемся открыть следующую страницу.
                            if (pb.isPermanentView()) {
                                pb.update();
                                continue;
                            }

                            pb.toPage(nextPageIndex, nextPage);

                            pb.update();
                            continue;
                        }
                    }
                    pb.update();
                }
                pb.addUpdatePageCount(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
