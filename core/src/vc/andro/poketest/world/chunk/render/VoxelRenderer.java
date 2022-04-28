package vc.andro.poketest.world.chunk.render;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.world.chunk.Chunk;

public interface VoxelRenderer {
    void render(@NotNull Chunk chunk, byte voxel, int lx, int y, int lz, int wx, int wz, @NotNull VertexArray vertexArray8f,
                @NotNull IndexArray indices, @Nullable VoxelAttributes attrs);
}
