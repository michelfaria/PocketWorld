package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.voxel.Voxel;
import vc.andro.poketest.voxel.VoxelPools;

public class Chunk implements Pool.Poolable {

    public static final Pool<Chunk> POOL = Pools.get(Chunk.class);
    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    public World world;
    public int cx;
    public int cz;

    protected Voxel[][][] voxels;
    protected int voxelCount; // Amount of voxels that exist in this chunk
    protected ChunkRenderingStrategy chunkRenderingStrategy;

    private Chunk() {
    }

    public void init(World world, int cx, int cz) {
        this.world = world;
        this.cx = cx;
        this.cz = cz;
        voxels = new Voxel[CHUNK_SIZE][CHUNK_DEPTH][CHUNK_SIZE];
        chunkRenderingStrategy = new ChunkRenderingStrategy(this);
    }

    @Override
    public void reset() {
        world = null;
        cx = 0;
        cz = 0;
        voxels = null;
        voxelCount = 0;
        chunkRenderingStrategy = null;
    }

    public @Nullable
    Voxel getTileAt_LP(int lx, int y, int lz) {
        return voxels[lx][y][lz];
    }

    public void putTileAt(int lx, int wy, int lz, @Nullable Voxel voxel) {
        Voxel prevVoxel = getTileAt_LP(lx, wy, lz);
        if (prevVoxel == null && voxel != null) {
            voxelCount++;
        } else if (prevVoxel != null && voxel == null) {
            voxelCount--;
        }
        if (voxel != null) {
            voxel.storePosition(this, lx, wy, lz);
        }

        if (prevVoxel != null) {
            VoxelPools.free(prevVoxel);
        }

        voxels[lx][wy][lz] = voxel;
    }

    public void updateTiles() {
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int wy = 0; wy < CHUNK_DEPTH; wy++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    Voxel v = getTileAt_LP(lx, wy, lz);
                    if (v == null) {
                        continue;
                    }
                    v.doTileUpdate();
                }
            }
        }
    }

    public @Nullable
    Voxel getSurfaceTile_LP(int lx, int lz) {
        for (int y = CHUNK_DEPTH - 1; y >= 0; y--) {
            Voxel tile = getTileAt_LP(lx, y, lz);
            if (tile != null) {
                return tile;
            }
        }
        return null;
    }
}
