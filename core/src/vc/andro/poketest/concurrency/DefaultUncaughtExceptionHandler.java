package vc.andro.poketest.concurrency;

import com.badlogic.gdx.Gdx;

import java.io.PrintWriter;
import java.io.StringWriter;

class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static volatile DefaultUncaughtExceptionHandler sInstance = null;

    private DefaultUncaughtExceptionHandler() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + DefaultUncaughtExceptionHandler.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static DefaultUncaughtExceptionHandler getInstance() {
        if (sInstance == null) {
            synchronized (DefaultUncaughtExceptionHandler.class) {
                if (sInstance == null) {
                    sInstance = new DefaultUncaughtExceptionHandler();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        stringWriter.write("Uncaught exception in thread \"%s!\"\n".formatted(thread.getName()));
        throwable.printStackTrace(printWriter);
        Gdx.app.log("Threads", stringWriter.toString());
        System.exit(1);
    }
}
