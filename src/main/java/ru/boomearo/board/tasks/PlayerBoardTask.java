package ru.boomearo.board.tasks;

import ru.boomearo.board.objects.PlayerBoard;

public class PlayerBoardTask implements Runnable{

    private final PlayerBoard playerBoard;

    public PlayerBoardTask(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    public void run() {
        try {
            this.playerBoard.handleBoard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
