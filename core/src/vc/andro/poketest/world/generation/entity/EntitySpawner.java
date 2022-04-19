package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.world.Chunk;

public interface EntitySpawner<T extends ProspectorResult> {
    void spawnEntity(T prospectorResult, Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz);
}
