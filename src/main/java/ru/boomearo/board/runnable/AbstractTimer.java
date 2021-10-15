package ru.boomearo.board.runnable;

import java.util.concurrent.TimeUnit;

import ru.boomearo.board.Board;

public abstract class AbstractTimer extends Thread {

    private volatile boolean cancel = false;

    private final long time;

    private boolean silent;

    public AbstractTimer(String name, TimeUnit unit, long time) {
        super(name + "-Thread");
        this.time = unit.toMillis(time);
        this.silent = false;
    }

    public AbstractTimer(String name, TimeUnit unit, long time, boolean silent) {
        super(name + "-Thread");
        this.time = unit.toMillis(time);
        this.silent = silent;
    }

    @Override
    public void run() {
        if (!this.silent) {
            Board.getInstance().getLogger().info(this.getName() + " успешно запущен!");
        }
        while (!this.cancel) {
            try {
                Thread.sleep(this.time);

                task();
            }
            catch (Throwable t) {
                if (!this.silent) {
                    Board.getInstance().getLogger().warning(this.getName() + " успешно был прерван!");
                }
                return;
            }
        }
        if (!this.silent) {
            Board.getInstance().getLogger().warning(this.getName() + " успешно завершен!");
        }
    }

    public void cancelTask(boolean force) {
        this.cancel = true;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public abstract void task() throws Throwable;
}
