package vc.andro.poketest.world;

import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.voxel.BasicVoxel;

public class Chunk {

    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    public final World world;
    public final int cx;
    public final int cz;

    protected final BasicVoxel[][][] voxels;
    protected int voxelCount; // Amount of voxels that exist in this chunk
    protected final ChunkRenderingStrategy chunkRenderingStrategy;

    public Chunk(World world, int cx, int cz) {
        this.world = world;
        this.cx = cx;
        this.cz = cz;
        voxels = new BasicVoxel[CHUNK_SIZE][CHUNK_DEPTH][CHUNK_SIZE];
        chunkRenderingStrategy = new ChunkRenderingStrategy(this);
    }

    public @Nullable
    BasicVoxel getTileAt_LP(int lx, int y, int lz) {
        return voxels[lx][y][lz];
    }

    public void putTileAt(int lx, int wy, int lz, @Nullable BasicVoxel voxel) {
        BasicVoxel prevVoxel = getTileAt_LP(lx, wy, lz);
        if (prevVoxel == null && voxel != null) {
            voxelCount++;
        } else if (prevVoxel != null && voxel == null) {
            voxelCount--;
        }
        if (voxel != null) {
            voxel.storePosition(this, lx, wy, lz);
        }
        voxels[lx][wy][lz] = voxel;
    }

    public void updateTiles() {
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int wy = 0; wy < CHUNK_DEPTH; wy++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    BasicVoxel v = getTileAt_LP(lx, wy, lz);
                    if (v == null) {
                        continue;
                    }
                    v.doTileUpdate();
                }
            }
        }
    }

    public @Nullable
    BasicVoxel getSurfaceTile_LP(int lx, int lz) {
        for (int y = CHUNK_DEPTH - 1; y >= 0; y--) {
            BasicVoxel tile = getTileAt_LP(lx, y, lz);
            if (tile != null) {
                return tile;
            }
        }
        return null;
    }
}
