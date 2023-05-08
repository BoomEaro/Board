package ru.boomearo.board.tasks;

import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.objects.PlayerBoard;

public class BoardUpdateTask implements Runnable {

    private final BoardManager boardManager;

    public BoardUpdateTask(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Override
    public void run() {
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
