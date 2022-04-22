package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import vc.andro.poketest.util.ArrayUtil;

import java.util.Arrays;

public class Chunk implements Pool.Poolable {

    public static final Pool<Chunk> POOL = Pools.get(Chunk.class);

    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    public World world;
    public int cx;
    public int cz;

    private final byte[] voxels;
    protected int voxelCount; // Amount of voxels that exist in this chunk
    protected ChunkRenderingStrategy chunkRenderingStrategy;

    private Chunk() {
        voxels = new byte[CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE];
    }

    public void init(World world, int cx, int cz) {
        this.world = world;
        this.cx = cx;
        this.cz = cz;
        chunkRenderingStrategy = new ChunkRenderingStrategy(this);
    }

    @Override
    public void reset() {
        world = null;
        cx = 0;
        cz = 0;
        voxelCount = 0;
        Arrays.fill(voxels, (byte) 0);
        chunkRenderingStrategy = null;
    }

    public byte getVoxelAt_LP(int lx, int ly, int lz) {
        return voxels[calcVoxelArrayPosition_LP(lx, ly, lz)];
    }

    private int calcVoxelArrayPosition_LP(int lx, int ly, int lz) {
        return ArrayUtil.xyzToI(CHUNK_SIZE, CHUNK_DEPTH, lx, ly, lz);
    }

    public void putVoxelAt_LP(int lx, int ly, int lz, byte voxel) {
        byte prevVoxel = getVoxelAt_LP(lx, ly, lz);
        if (prevVoxel == 0 && voxel != 0) {
            voxelCount++;
        } else if (prevVoxel != 0 && voxel == 0) {
            voxelCount--;
        }
        voxels[calcVoxelArrayPosition_LP(lx, ly, lz)] = voxel;
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

    public synchronized int getSurfaceVoxelWy_LP(int lx, int lz) {
        for (int wy = CHUNK_DEPTH - 1; wy >= 0; wy--) {
            byte v = getVoxelAt_LP(lx, wy, lz);
            if (v != 0) {
                return wy;
            }
        }
        return -1;
    }
}
