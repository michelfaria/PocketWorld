package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.world.Chunk;

import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class WorldGenEntitySpawner<T extends ProspectorResult> {

    private final SpawnProspector<T> prospector;
    private final EntitySpawner<T> entitySpawner;

    public WorldGenEntitySpawner(SpawnProspector<T> prospector, EntitySpawner<T> entitySpawner) {
        this.prospector = prospector;
        this.entitySpawner = entitySpawner;
    }

    public void spawnEntitiesInChunk(Chunk chunk) {
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                int wx = LxWx(chunk.cx, lx);
                int wz = LzWz(chunk.cz, lz);
                T result = prospector.prospect(chunk, wx, wz, chunk.cx, chunk.cz, lx, lz);
                if (result.shouldSpawn) {
                    assert result.spawnY != null : "Prospector should have determined Y position";
                    entitySpawner.spawnEntity(result, chunk, wx, result.spawnY, wz, chunk.cx, chunk.cz, lx, lz);
                }
            }
        }
    }

}
