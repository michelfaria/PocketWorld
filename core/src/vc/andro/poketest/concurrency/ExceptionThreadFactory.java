package vc.andro.poketest.concurrency;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ExceptionThreadFactory implements ThreadFactory {

    private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
    private final @NotNull Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    ExceptionThreadFactory() {
        uncaughtExceptionHandler = DefaultUncaughtExceptionHandler.getInstance();
    }

    ExceptionThreadFactory(@NotNull Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        Thread thread = defaultFactory.newThread(runnable);
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        return thread;
    }
}
