package vc.andro.poketest.world;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.util.ArrayUtil;
import vc.andro.poketest.voxel.VoxelAttributes;

import java.util.Arrays;

public class Chunk implements Pool.Poolable {

    public static final Pool<Chunk> POOL = Pools.get(Chunk.class);
    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    public World world;
    public int cx;
    public int cz;
    private final byte[] voxels;
    private final IntMap<VoxelAttributes> voxelAttributesMap;
    protected int voxelCount; // Amount of voxels that exist in this chunk
    protected ChunkRenderingStrategy chunkRenderingStrategy;

    private Chunk() {
        voxels = new byte[CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE];
        voxelAttributesMap = new IntMap<>();
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
        Arrays.fill(voxels, (byte) 0);
        {
            for (VoxelAttributes a : voxelAttributesMap.values()) {
                VoxelAttributes.POOL.free(a);
            }
            voxelAttributesMap.clear();
        }
        voxelCount = 0;
        chunkRenderingStrategy = null;
    }

    /**
     * Gets the voxel at (lx,ly,lz)
     *
     * @param lx Local chunk x
     * @param ly Local chunk y
     * @param lz Local chunk z
     * @return Voxel
     */
    public byte getVoxelAt_LP(int lx, int ly, int lz) {
        return voxels[calcVoxelArrayPosition_LP(lx, ly, lz)];
    }

    /**
     * Gets the VoxelAttributes for a voxel at (lx,ly,lz).
     *
     * @param lx Local chunk x
     * @param ly Local chunk y
     * @param lz Local chunk z
     * @return VoxelAttributes or null
     */
    @Nullable
    public VoxelAttributes getVoxelAttrsAt_LP(int lx, int ly, int lz) {
        return voxelAttributesMap.get(calcVoxelArrayPosition_LP(lx, ly, lz));
    }

    /**
     * Gets the VoxelAttributes for a voxel at (lx,ly,lz) or create a new one and return it.
     *
     * @param lx Local chunk x
     * @param ly Local chunk y
     * @param lz Local chunk z
     * @return VoxelAttributes or null
     */
    public VoxelAttributes getVoxelAttrsAt_G_LP(int lx, int ly, int lz) {
        VoxelAttributes attrs = voxelAttributesMap.get(calcVoxelArrayPosition_LP(lx, ly, lz));
        if (attrs == null) {
            attrs = VoxelAttributes.POOL.obtain();
            putVoxelAttrsAt_LP(lx, ly, lz, attrs);
        }
        return attrs;
    }

    /**
     * Puts a VoxelAttribute for a voxel at the given (lx,ly,lz) position. If a VoxelAttribute already exists there,
     * it is replaced.
     *
     * @param lx              Local chunk x
     * @param ly              Local chunk y
     * @param lz              Local chunk z
     * @param voxelAttributes
     */
    public void putVoxelAttrsAt_LP(int lx, int ly, int lz, @NotNull VoxelAttributes voxelAttributes) {
        int pos = calcVoxelArrayPosition_LP(lx, ly, lz);
        VoxelAttributes oldValue = voxelAttributesMap.put(pos, voxelAttributes);
        if (oldValue != null) {
            VoxelAttributes.POOL.free(oldValue);
        }
    }

    /**
     * Deletes a voxel attribute associated with a voxel at (lx, ly, lz).
     *
     * @param lx Local chunk X
     * @param ly Local chunk Y
     * @param lz Local chunk Z
     * @throws IllegalStateException If there is no voxel attribute associated with the voxel at position (lx, ly, lz).
     */
    public void delVoxelAttrsAt_LP(int lx, int ly, int lz) {
        delVoxelAttrsAt_LP(lx, ly, lz, false);
    }

    /**
     * Delete the voxel attributes for a voxel at (lx, ly, lz)
     *
     * @param lx                Local chunk X
     * @param ly                Local chunk Y
     * @param lz                Local chunk Z
     * @param ignoreNonexisting See <code>@throws</code>
     * @throws IllegalStateException Thrown if <code>ignoreNonexisting</code> is set to false, and there is no voxel
     *                               attribute associated with the voxel at position (lx, ly, lz).
     */
    public void delVoxelAttrsAt_LP(int lx, int ly, int lz, boolean ignoreNonexisting) {
        int pos = calcVoxelArrayPosition_LP(lx, ly, lz);
        VoxelAttributes removedValue = voxelAttributesMap.remove(pos);
        if (removedValue == null) {
            throw new IllegalStateException("No attribute for voxel (%d, %d, %d)".formatted(lx, ly, lz));
        }
        VoxelAttributes.POOL.free(removedValue);
    }

    /**
     * Calculates the access index of a CHUNK_SIZE^2 * CHUNK_DEPTH array for a given (lx,ly,lz) position.
     *
     * @param lx Local chunk x
     * @param ly Local chunk y
     * @param lz Local chunk z
     * @return
     */
    private int calcVoxelArrayPosition_LP(int lx, int ly, int lz) {
        return ArrayUtil.xyzToI(CHUNK_SIZE, CHUNK_DEPTH, lx, ly, lz);
    }

    /**
     * Puts a voxel at the given (lx,ly,lz). If a voxel already exists in that position, it will be replaced.
     *
     * @param lx    Local chunk x
     * @param ly    Local chunk y
     * @param lz    Local chunk z
     * @param voxel
     */
    public void putVoxelAt_LP(int lx, int ly, int lz, byte voxel) {
        byte prevVoxel = getVoxelAt_LP(lx, ly, lz);
        if (prevVoxel == 0 && voxel != 0) {
            voxelCount++;
        } else if (prevVoxel != 0 && voxel == 0) {
            voxelCount--;
            delVoxelAttrsAt_LP(lx, ly, lz, true);
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

    /**
     * Gets the top-most non-null voxel in this chunk at the specified (lx,lz) column.
     *
     * @param lx Local chunk x
     * @param lz Local chunk z
     * @return The voxel or invalid value (-1)
     */
    public int getSurfaceVoxelWy_LP(int lx, int lz) {
        for (int wy = CHUNK_DEPTH - 1; wy >= 0; wy--) {
            byte v = getVoxelAt_LP(lx, wy, lz);
            if (v != 0) {
                return wy;
            }
        }
        return -1;
    }
}
