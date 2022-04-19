package ru.boomearo.board.runnable;

import java.util.concurrent.TimeUnit;

import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

public class BoardUpdater extends AbstractTimer {

    private final BoardManager boardManager;

    //Если будет работать плохо из-за асинхронного обновления скорборда, то сделаем в основном потоке..
    public BoardUpdater(BoardManager boardManager) {
        super("BoardUpdater", TimeUnit.SECONDS, 1);
        this.boardManager = boardManager;
    }

    @Override
    public void task() {
        update();
    }

    public void update() {
        try {
            //Получаем все скорборды игроков и обрабатываем их
            for (PlayerBoard pb : this.boardManager.getAllPlayerBoards()) {
                pb.handleBoard();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
