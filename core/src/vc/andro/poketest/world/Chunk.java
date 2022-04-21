package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Chunk implements Pool.Poolable {

    public static final Pool<Chunk> POOL = Pools.get(Chunk.class);
    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    public World world;
    public int cx;
    public int cz;

    protected byte[][][] voxels;
    protected int voxelCount; // Amount of voxels that exist in this chunk
    protected ChunkRenderingStrategy chunkRenderingStrategy;

    private Chunk() {
    }

    public void init(World world, int cx, int cz) {
        this.world = world;
        this.cx = cx;
        this.cz = cz;
        voxels = new byte[CHUNK_SIZE][CHUNK_DEPTH][CHUNK_SIZE];
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

    public byte getVoxelAt_LP(int lx, int y, int lz) {
        return voxels[lx][y][lz];
    }

    public void putVoxelAt(int lx, int wy, int lz, byte voxel) {
        byte prevVoxel = getVoxelAt_LP(lx, wy, lz);
        if (prevVoxel == 0 && voxel != 0) {
            voxelCount++;
        } else if (prevVoxel != 0 && voxel == 0) {
            voxelCount--;
        }
        voxels[lx][wy][lz] = voxel;
    }

    public void updateVoxels() {
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int wy = 0; wy < CHUNK_DEPTH; wy++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    byte v = getVoxelAt_LP(lx, wy, lz);
                    if (v == 0) {
                        continue;
                    }
                    // FIXME
                    // v.doTileUpdate();
                }
            }
        }
    }

    public int getSurfaceVoxel_LP__SUPRETVAL__wy;

    public byte getSurfaceVoxel_LP(int lx, int lz) {
        for (int wy = CHUNK_DEPTH - 1; wy >= 0; wy--) {
            byte v = getVoxelAt_LP(lx, wy, lz);
            if (v != 0) {
                getSurfaceVoxel_LP__SUPRETVAL__wy = wy;
                return v;
            }
        }
        getSurfaceVoxel_LP__SUPRETVAL__wy = -1;
        return 0;
    }
}
