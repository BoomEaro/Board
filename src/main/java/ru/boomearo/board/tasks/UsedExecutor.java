package ru.boomearo.board.tasks;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Data
public class UsedExecutor {

    @ToString.Exclude
    private final ScheduledExecutorService executor;
    private AtomicInteger count = new AtomicInteger(0);

    public UsedExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void execute(Runnable runnable) {
        this.executor.execute(runnable);
    }

    public <T> CompletableFuture<T> submit(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, this.executor);
    }

    public ScheduledFuture<?> schedule(Runnable runnable, int delay, int period, TimeUnit timeUnit) {
        return this.executor.scheduleWithFixedDelay(runnable, delay, period, timeUnit);
    }

    public void shutdown() {
        this.executor.shutdown();
    }

    public void takeExecutor() {
        this.count.incrementAndGet();
    }

    public void restoreExecutor() {
        int newValue = this.count.decrementAndGet();
        if (newValue < 0) {
            this.count.set(0);
        }
    }
}
