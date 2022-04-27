package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;

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

    private final Array<Chunk> chunks = new Array<>(Chunk.class);

    @Override
    public void update(World world, float delta) {
        chunks.clear();
        world.getChunks().forEach(chunk -> {
            if (world.isChunkOutsideOfRenderDistance(chunk)) {
                chunks.add(chunk);
            }
        });
        for (Chunk chunk : chunks) {
            world.unloadChunk(chunk);
        }
    }
}