package vc.andro.poketest.world;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Direction;
import vc.andro.poketest.util.ArrayUtil;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.VoxelSpec;
import vc.andro.poketest.voxel.VoxelSpecs;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class Chunk implements Pool.Poolable {

    public static final Pool<Chunk> POOL = Pools.get(Chunk.class);
    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    private World world;
    private int cx;
    private int cz;
    private final byte[] voxels;
    private final IntMap<VoxelAttributes> voxelAttributesMap;
    private int voxelCount; // Amount of voxels that exist in this chunk
    private ChunkRenderingStrategy chunkRenderingStrategy;
    private boolean initialized;

    private final ReentrantReadWriteLock lock;
    private final Lock writeLock;
    private final Lock readLock;

    private Chunk() {
        voxels = new byte[CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE];
        voxelAttributesMap = new IntMap<>();
        lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }

    public void init(@NotNull World world, int cx, int cz) {
        this.world = world;
        this.cx = cx;
        this.cz = cz;
        chunkRenderingStrategy = new ChunkRenderingStrategy(this);
        initialized = true;
    }

    @Override
    public void reset() {
        world = null;
        cx = 0;
        cz = 0;
        Arrays.fill(voxels, (byte) 0);
        { // reset: voxelAttributesMap
            for (VoxelAttributes a : voxelAttributesMap.values()) {
                VoxelAttributes.POOL.free(a);
            }
            voxelAttributesMap.clear();
        }
        voxelCount = 0;
        chunkRenderingStrategy = null;
        initialized = false;
    }

    private void throwIfUninitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException("Attempted to perform an operation on an uninitialized chunk");
        }
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
        throwIfUninitialized();
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
        throwIfUninitialized();
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
        throwIfUninitialized();
        VoxelAttributes attrs = getVoxelAttrsAt_LP(lx, ly, lz);
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
        throwIfUninitialized();
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
        throwIfUninitialized();
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
        throwIfUninitialized();
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
        throwIfUninitialized();
        return ArrayUtil.xyzToI(CHUNK_SIZE, CHUNK_DEPTH, lx, ly, lz);
    }

    /**
     * Puts a voxel at the given (lx,ly,lz). If a voxel already exists in that position, it will be replaced.
     *
     * @param lx    Local chunk x
     * @param ly    Local chunk y
     * @param lz    Local chunk z
     * @param voxel Voxel to put
     */
    public void putVoxelAt_LP(int lx, int ly, int lz, byte voxel) {
        throwIfUninitialized();
        putVoxelAt_LP(lx, ly, lz, voxel, false);
    }

    /**
     * Puts a voxel at the given (lx,ly,lz). If a voxel already exists in that position, it will be replaced.
     *
     * @param lx             Local chunk x
     * @param ly             Local chunk y
     * @param lz             Local chunk z
     * @param voxel          Voxel to put
     * @param keepAttributes If set to true, the attributes of the previous voxel won't be deleted.
     */
    public void putVoxelAt_LP(int lx, int ly, int lz, byte voxel, boolean keepAttributes) {
        throwIfUninitialized();
        byte prevVoxel = getVoxelAt_LP(lx, ly, lz);
        if (prevVoxel == 0 && voxel != 0) {
            voxelCount++;
        } else if (prevVoxel != 0 && voxel == 0) {
            voxelCount--;
            if (!keepAttributes) {
                delVoxelAttrsAt_LP(lx, ly, lz, true);
            }
        }
        voxels[calcVoxelArrayPosition_LP(lx, ly, lz)] = voxel;
        chunkRenderingStrategy.setNeedsRenderingUpdate();
    }

    public void updateVoxels() {
        throwIfUninitialized();
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
        throwIfUninitialized();
        for (int wy = CHUNK_DEPTH - 1; wy >= 0; wy--) {
            byte v = getVoxelAt_LP(lx, wy, lz);
            if (v != 0) {
                return wy;
            }
        }
        return -1;
    }

    /**
     * Slopifies every voxel in this chunk if they need to become slopes.
     */
    public void slopifyVoxels(boolean propagateToSurroundingChunks) {
        throwIfUninitialized();
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    slopifyVoxel_LP(lx, y, lz, propagateToSurroundingChunks);
                }
            }
        }
    }

    /**
     * Turns a voxel at (lx, y, lz) into a slope if it meets the conditions to be a slope.
     *
     * @param lx                           Chunk local x
     * @param y                            y
     * @param lz                           Chunk local z
     * @param propagateToSurroundingChunks If set to true, sloping will propagate to adjacent voxels that are outside the specified chunk
     */
    private void slopifyVoxel_LP(int lx, int y, int lz, boolean propagateToSurroundingChunks) {
        throwIfUninitialized();
        byte voxel = getVoxelAt_LP(lx, y, lz);
        if (voxel == 0) {
            return;
        }
        VoxelSpec voxelSpec = VoxelSpecs.VOXEL_TYPES[voxel];
        if (!voxelSpec.canBeSloped) {
            return;
        }

        int wx = LxWx(cx, lx);
        int wz = LzWz(cz, lz);

        boolean isVoxelAbove = y < CHUNK_DEPTH - 1 && getWorld().getVoxelAt_WP(wx, y + 1, wz) != 0;
        if (isVoxelAbove) {
            return;
        }

        boolean isVoxelBelow = y > 0 && getWorld().getVoxelAt_WP(wx, y - 1, wz) != 0;
        if (!isVoxelBelow) {
            return;
        }

        byte voxelWest = world.getVoxelAt_WP(wx - 1, y, wz);
        byte voxelEast = world.getVoxelAt_WP(wx + 1, y, wz);
        byte voxelSouth = world.getVoxelAt_WP(wx, y, wz + 1);
        byte voxelNorth = world.getVoxelAt_WP(wx, y, wz - 1);
        byte voxelNorthwest = world.getVoxelAt_WP(wx - 1, y, wz - 1);
        byte voxelNortheast = world.getVoxelAt_WP(wx + 1, y, wz - 1);
        byte voxelSouthwest = world.getVoxelAt_WP(wx - 1, y, wz + 1);
        byte voxelSoutheast = world.getVoxelAt_WP(wx + 1, y, wz + 1);

        /*
          Southwest-facing corner
          ▢ ▢
          ▢ ◢
         */
        if (voxelNorthwest == 0 && voxelWest == 0 && voxelNorth == 0) {
            slopifyVoxel(lx, y, lz, Direction.NORTHWEST, false);
        }
        /*
          Northeast-facing corner
          ▢ ▢
          ◣ ▢
         */
        else if (voxelNorth == 0 && voxelNortheast == 0 && voxelEast == 0) {
            slopifyVoxel(lx, y, lz, Direction.NORTHEAST, false);
        }
        /*
          Southwest-facing corner
          ▢ ◥
          ▢ ▢
         */
        else if (voxelSouthwest == 0 && voxelWest == 0 && voxelSouth == 0) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHWEST, false);
        }
        /*
          Southeast-facing corner
          ◤ ▢
          ▢ ▢
         */
        else if (voxelSoutheast == 0 && voxelEast == 0 && voxelSouth == 0) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHEAST, false);
        }
        /*
          Northwest-facing inner corner
          ▢ |
          _ 」←
         */
        else if (voxelNorthwest == 0 && voxelWest > 0 && voxelNorth > 0) {
            slopifyVoxel(lx, y, lz, Direction.NORTHWEST, true);
        }
        /*
         Northeast-facing inner corner
           | ▢
         → ⌞ _
         */
        else if (voxelNortheast == 0 && voxelEast > 0 && voxelNorth > 0) {
            slopifyVoxel(lx, y, lz, Direction.NORTHEAST, true);
        }
        /*
         Southwest-facing inner corner
            ̅ ⌝ ←
           ▢ |
         */
        else if (voxelSouthwest == 0 && voxelWest > 0 && voxelSouth > 0) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHWEST, true);
        }
        /*
         Southeast-facing inner corner
          → ⌜  ̅
            | ▢
         */
        else if (voxelSoutheast == 0 && voxelEast > 0 && voxelSouth > 0) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHEAST, true);
        }
        /*  West edge */
        else if (voxelWest == 0) {
            slopifyVoxel(lx, y, lz, Direction.WEST, false);
        }
        /* East edge */
        else if (voxelEast == 0) {
            slopifyVoxel(lx, y, lz, Direction.EAST, false);
        }
        /* South edge */
        else if (voxelSouth == 0) {
            slopifyVoxel(lx, y, lz, Direction.SOUTH, false);
        }
        /* North edge */
        else if (voxelNorth == 0) {
            slopifyVoxel(lx, y, lz, Direction.NORTH, false);
        }

        if (propagateToSurroundingChunks) {
            if (lx == 0 || lz == 0 || lx == CHUNK_SIZE - 1 || lz == CHUNK_SIZE - 1) {
                int cxΔ = 0;
                if (lx == 0) {
                    cxΔ = -1;
                } else if (lx == CHUNK_SIZE - 1) {
                    cxΔ = 1;
                }
                int czΔ = 0;
                if (lz == 0) {
                    czΔ = -1;
                } else if (lz == CHUNK_SIZE - 1) {
                    czΔ = 1;
                }
                Chunk c = world.getChunkAt_CP(getCx() + cxΔ, getCz() + czΔ);
                if (c != null) {
                    c.slopifyVoxels(false);
                }
            }
        }
    }

    private void slopifyVoxel(int lx, int y, int lz, byte slopeDirection, boolean isInnerCorner) {
        throwIfUninitialized();
        VoxelAttributes attrs = getVoxelAttrsAt_G_LP(lx, y, lz);
        attrs.configureSlope(slopeDirection, isInnerCorner);
        byte voxel = getVoxelAt_LP(lx, y, lz);
        if (VoxelSpecs.VOXEL_TYPES[voxel] == VoxelSpecs.GRASS) {
            putVoxelAt_LP(lx, y, lz, VoxelSpecs.getVoxelId(VoxelSpecs.DIRT), true);
        }
    }

    @Deprecated
    public void forceRerender() {
        throwIfUninitialized();
        chunkRenderingStrategy.setNeedsRenderingUpdate();
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "cx=" + cx +
                ", cz=" + cz +
                '}';
    }

    public World getWorld() {
        throwIfUninitialized();
        return world;
    }

    public int getCx() {
        throwIfUninitialized();
        return cx;
    }

    public int getCz() {
        throwIfUninitialized();
        return cz;
    }

    public ChunkRenderingStrategy getChunkRenderingStrategy() {
        throwIfUninitialized();
        return chunkRenderingStrategy;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Lock getWriteLock() {
        throwIfUninitialized();
        return writeLock;
    }

    public Lock getReadLock() {
        throwIfUninitialized();
        return readLock;
    }
}
