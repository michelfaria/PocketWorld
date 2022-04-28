package vc.andro.poketest.world.chunk.render;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.VoxelSpecs;
import vc.andro.poketest.voxel.rendering.faces.FaceGenerationStrategy;
import vc.andro.poketest.world.chunk.Chunk;

import static vc.andro.poketest.world.chunk.Chunk.CHUNK_DEPTH;

public class DefaultVoxelRenderingStrategy implements VoxelRenderingStrategy {

    private static volatile DefaultVoxelRenderingStrategy sInstance = null;

    private DefaultVoxelRenderingStrategy() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + DefaultVoxelRenderingStrategy.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static DefaultVoxelRenderingStrategy getInstance() {
        if (sInstance == null) {
            synchronized (DefaultVoxelRenderingStrategy.class) {
                if (sInstance == null) {
                    sInstance = new DefaultVoxelRenderingStrategy();
                }
            }
        }
        return sInstance;
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

        FaceGenerationStrategy fgs = VoxelSpecs.getSpecForVoxel(voxel).faceGenerationStrategy;
        if (fgs == null) {
            return;
        }

        if (voxelAbove <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y + 1, wz)) {
            fgs.createTopVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelUnder <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y - 1, wz)) {
            fgs.createBottomVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelWest <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx - 1, y, wz)) {
            fgs.createWestVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelEast <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx + 1, y, wz)) {
            fgs.createEastVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelNorth <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y, wz - 1)) {
            fgs.createNorthVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
        if (voxelSouth <= 0 || chunk.getWorld().isVoxelAtPosEffectivelyTransparent_WP(wx, y, wz + 1)) {
            fgs.createSouthVertices(vertexArray8f, indices, voxel, attrs, wx, y, wz);
        }
    }
}
