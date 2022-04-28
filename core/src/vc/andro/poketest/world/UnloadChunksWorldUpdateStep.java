package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import vc.andro.poketest.world.chunk.Chunk;

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

    private final Array<Chunk> aux = new Array<>(Chunk.class);

    @Override
    public synchronized void update(World world, float delta) {
        assert aux.size == 0 : "unexpected dirty auxiliary array";
        try {
            world.getChunks().forEach(chunk -> {
                if (world.isChunkOutsideOfRenderDistance(chunk)) {
                    aux.add(chunk);
                }
            });
            for (Chunk chunk : aux) {
                world.unloadChunk(chunk);
            }
        } finally {
            aux.clear();
        }
    }
}