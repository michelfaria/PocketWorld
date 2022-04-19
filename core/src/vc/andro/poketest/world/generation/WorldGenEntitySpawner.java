package vc.andro.poketest.world.generation;

import vc.andro.poketest.world.Chunk;

import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class WorldGenEntitySpawner {

    private final SpawnProspector prospector;
    private final EntitySpawner entitySpawner;

    public WorldGenEntitySpawner(SpawnProspector prospector, EntitySpawner entitySpawner) {
        this.prospector = prospector;
        this.entitySpawner = entitySpawner;
    }

    public void spawnEntitiesInChunk(Chunk chunk) {
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                int wx = LxWx(chunk.cx, lx);
                int wz = LzWz(chunk.cz, lz);
                if (prospector.meetsCondition(chunk, wx, wz, chunk.cx, chunk.cz, lx, lz)) {
                    int spawnY = prospector.getSpawnY();
                    entitySpawner.spawnEntity(chunk, wx, spawnY, wz, chunk.cx, chunk.cz, lx, lz);
                }
            }
        }
    }

    public interface SpawnProspector {
        boolean meetsCondition(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz);

        int getSpawnY();
    }

    public interface EntitySpawner {
        void spawnEntity(Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz);
    }
}
