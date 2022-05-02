package vc.andro.poketest.world.chunk.render;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;
import vc.andro.poketest.voxel.Voxel;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.Voxels;
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
            @NotNull Chunk chunk, Voxel voxel, int lx, int y, int lz, int wx, int wz,
            @NotNull VertexArray vertexArray8f, @NotNull IndexArray indices, @Nullable VoxelAttributes attrs) {
        Voxel voxelAbove = y < CHUNK_DEPTH - 1 ? chunk.getVoxelAt_LP(lx, y + 1, lz) : null;
        Voxel voxelUnder = y > 0 ? chunk.getVoxelAt_LP(lx, y - 1, lz) : null;
        Voxel voxelEast = chunk.getWorld().getVoxelAt_WP(wx + 1, y, wz);
        Voxel voxelWest = chunk.getWorld().getVoxelAt_WP(wx - 1, y, wz);
        Voxel voxelNorth = chunk.getWorld().getVoxelAt_WP(wx, y, wz - 1);
        Voxel voxelSouth = chunk.getWorld().getVoxelAt_WP(wx, y, wz + 1);

        if (Voxels.isAirOrNull(voxelAbove) || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y + 1, wz)) {
            faceGenerator.createTopVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (Voxels.isAirOrNull(voxelUnder) || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y - 1, wz)) {
            faceGenerator.createBottomVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (Voxels.isAirOrNull(voxelWest) || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx - 1, y, wz)) {
            faceGenerator.createWestVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (Voxels.isAirOrNull(voxelEast) || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx + 1, y, wz)) {
            faceGenerator.createEastVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (Voxels.isAirOrNull(voxelNorth) || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y, wz - 1)) {
            faceGenerator.createNorthVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (Voxels.isAirOrNull(voxelSouth) || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y, wz + 1)) {
            faceGenerator.createSouthVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
    }
}
