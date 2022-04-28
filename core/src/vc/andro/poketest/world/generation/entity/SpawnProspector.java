package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.world.chunk.Chunk;

public interface SpawnProspector<T extends ProspectorResult> {
    T prospect(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz);
}
