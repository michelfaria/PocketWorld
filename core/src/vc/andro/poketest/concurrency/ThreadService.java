package vc.andro.poketest.concurrency;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public final class ThreadService {

    private static final ThreadFactory threadFactory = new ExceptionThreadFactory();
    private static final ExecutorService executorService = Executors.newCachedThreadPool(threadFactory);

    public static <T> Future<T> submit(@NotNull Callable<T> task) {
        return executorService.submit(task);
    }

    public static <T> Future<T> submit(@NotNull Runnable task, T result) {
        return executorService.submit(task, result);
    }

    public static Future<?> submit(@NotNull Runnable task) {
        return executorService.submit(task);
    }

    private ThreadService() {
    }
}
