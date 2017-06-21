package com.dianrong.crnetwork.framework.adapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PengFeifei on 17-6-13.
 */

public class SchedulerAdapter {

    //// TODO: 17-6-21 升级rx导致类型不匹配 编译失败
    public static final Scheduler backScheduler = Schedulers.from(ExecutorManager.get().getBackExecutor());
    public static final Scheduler uiScheduler = AndroidSchedulers.mainThread();

    private static class ExecutorManager {

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
}
