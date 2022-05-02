package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import vc.andro.poketest.world.chunk.Chunk;

public class UnloadChunksWorldUpdateStep implements WorldUpdateStep {

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