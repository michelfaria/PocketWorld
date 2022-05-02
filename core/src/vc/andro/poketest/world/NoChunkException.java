package vc.andro.poketest.world;

public class NoChunkException extends RuntimeException {
    public NoChunkException() {
    }

    public NoChunkException(String message) {
        super(message);
    }

    public NoChunkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoChunkException(Throwable cause) {
        super(cause);
    }

    public NoChunkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
