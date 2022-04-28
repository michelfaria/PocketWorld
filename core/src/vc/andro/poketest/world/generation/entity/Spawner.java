package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.world.chunk.Chunk;

public interface Spawner<T extends ProspectorResult> {
    void spawn(T prospectorResult, Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz);
}
