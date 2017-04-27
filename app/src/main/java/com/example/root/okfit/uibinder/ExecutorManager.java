package com.example.root.okfit.uibinder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Lei Guoting on 17-3-30.
 */
public class ExecutorManager {

    private static final int KEEP_ALIVE_SECONDS = 60;

    private static class Singleton {
        static final ExecutorManager manger = new ExecutorManager();
    }

    private final ExecutorService backExecutor;

    private ExecutorManager() {
        int cpuNumber = Runtime.getRuntime().availableProcessors();
        int corePoolSize = Math.max(2, Math.min(cpuNumber - 1, 4));
        int maximumPoolSize = cpuNumber * 2 + 1;

        final BlockingQueue<Runnable> poolWorkQueue = new LinkedBlockingQueue<>(128);
        backExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                poolWorkQueue, new SimpleThreadFactory());
    }

    public static ExecutorManager get() {
        return Singleton.manger;
    }

    public Executor getBackExecutor() {
        return backExecutor;
    }

    private static class SimpleThreadFactory implements ThreadFactory {
        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format("BackService-%1$d", count.incrementAndGet()));
        }
    }
}
