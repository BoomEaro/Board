package ru.boomearo.board.runnable;

import java.util.concurrent.TimeUnit;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.PlayerBoard;

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
            //Получаем все скорборды игроков и обрабатываем их
            for (PlayerBoard pb : Board.getInstance().getBoardManager().getAllPlayerBoards()) {
                pb.handleBoard();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
