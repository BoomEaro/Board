package ru.boomearo.board.tasks;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

@Data
public class BalancedThreadPool {

    private final UsedExecutor[] usedExecutors;

    public BalancedThreadPool(String name, int size) {
        UsedExecutor[] usedExecutors = new UsedExecutor[size];
        for (int i = 0; i < size; i++) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder()
                    .setNameFormat(name + "-BalancedThread-%d-" + i)
                    .build();
            ScheduledThreadPoolExecutor scheduledExecutorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, threadFactory);
            scheduledExecutorService.setRemoveOnCancelPolicy(true);
            usedExecutors[i] = new UsedExecutor(scheduledExecutorService);
        }
        this.usedExecutors = usedExecutors;
    }

    public UsedExecutor getFreeExecutor() {
        int min = Integer.MAX_VALUE;
        UsedExecutor freeExecutor = this.usedExecutors[0];
        for (UsedExecutor usedExecutor : this.usedExecutors) {
            int count = usedExecutor.getCount().get();
            if (count < min) {
                min = count;
                freeExecutor = usedExecutor;
            }
        }

        return freeExecutor;
    }

    public void shutdown() {
        for (UsedExecutor usedExecutor : this.usedExecutors) {
            usedExecutor.shutdown();
        }
    }
}