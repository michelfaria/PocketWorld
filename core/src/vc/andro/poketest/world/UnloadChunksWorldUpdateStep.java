package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;

import java.util.concurrent.locks.Lock;

public class UnloadChunksWorldUpdateStep implements WorldUpdateStep {

    private static volatile UnloadChunksWorldUpdateStep sInstance = null;

    private UnloadChunksWorldUpdateStep() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + UnloadChunksWorldUpdateStep.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static UnloadChunksWorldUpdateStep getInstance() {
        if (sInstance == null) {
            synchronized (UnloadChunksWorldUpdateStep.class) {
                if (sInstance == null) {
                    sInstance = new UnloadChunksWorldUpdateStep();
                }
            }
        }
        return sInstance;
    }

    private final Array<Chunk> aux           = new Array<>(Chunk.class);
    private final Array<Lock>  locksAcquired = new Array<>(Lock.class);

    @Override
    public synchronized void update(World world, float delta) {
        assert aux.size == 0 : "unexpected dirty auxiliary array";
        assert locksAcquired.size == 0 : "unexpected dirty auxiliary array";

        world.getChunksLock().writeLock().lock();
        try {
            world.getChunks().forEach(chunk -> {
                Lock writeLock = chunk.getLock().writeLock();
                writeLock.lock();
                locksAcquired.add(writeLock);

                if (world.isChunkOutsideOfRenderDistance(chunk)) {
                    aux.add(chunk);
                }
            });
            for (Chunk chunk : aux) {
                world.unloadChunk(chunk);
            }
        } finally {
            world.getChunksLock().writeLock().unlock();

            aux.clear();
            for (Lock lock : locksAcquired) {
                lock.unlock();
            }
            locksAcquired.clear();
        }
    }
}