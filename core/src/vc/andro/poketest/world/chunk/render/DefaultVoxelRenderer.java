package vc.andro.poketest.world.chunk.render;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.rendering.faces.FaceGenerator;
import vc.andro.poketest.world.chunk.Chunk;

import static vc.andro.poketest.world.chunk.Chunk.CHUNK_DEPTH;

public class DefaultVoxelRenderer implements VoxelRenderer {

    private final @NotNull FaceGenerator faceGenerator;

    public DefaultVoxelRenderer(@NotNull FaceGenerator faceGenerator) {
        this.faceGenerator = faceGenerator;
    }

    @Override
    public void render(
            @NotNull Chunk chunk, byte voxel, int lx, int y, int lz, int wx, int wz,
            @NotNull VertexArray vertexArray8f, @NotNull IndexArray indices, @Nullable VoxelAttributes attrs) {
        byte voxelAbove = y < CHUNK_DEPTH - 1 ? chunk.getVoxelAt_LP(lx, y + 1, lz) : -1;
        byte voxelUnder = y > 0 ? chunk.getVoxelAt_LP(lx, y - 1, lz) : -1;
        byte voxelEast = chunk.getWorld().getVoxelAt_WP(wx + 1, y, wz);
        byte voxelWest = chunk.getWorld().getVoxelAt_WP(wx - 1, y, wz);
        byte voxelNorth = chunk.getWorld().getVoxelAt_WP(wx, y, wz - 1);
        byte voxelSouth = chunk.getWorld().getVoxelAt_WP(wx, y, wz + 1);

        if (voxelAbove <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y + 1, wz)) {
            faceGenerator.createTopVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelUnder <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y - 1, wz)) {
            faceGenerator.createBottomVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelWest <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx - 1, y, wz)) {
            faceGenerator.createWestVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelEast <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx + 1, y, wz)) {
            faceGenerator.createEastVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelNorth <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y, wz - 1)) {
            faceGenerator.createNorthVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelSouth <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y, wz + 1)) {
            faceGenerator.createSouthVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
    }
}
