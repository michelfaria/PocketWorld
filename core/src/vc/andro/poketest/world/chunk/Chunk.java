package vc.andro.poketest.world.chunk;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Direction;
import vc.andro.poketest.util.ArrayUtil;
import vc.andro.poketest.voxel.Voxel;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.Voxels;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.chunk.render.ChunkRenderer;

import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class Chunk implements Disposable {

    public static final int CHUNK_SIZE   = 16;
    public static final int CHUNK_DEPTH  = 128;
    public static final int TOTAL_VOXELS = CHUNK_SIZE * CHUNK_DEPTH * CHUNK_SIZE;

    private final byte[] voxels = new byte[TOTAL_VOXELS];

    private final World                   world;
    private final int                     cx;
    private final int                     cz;
    private final IntMap<VoxelAttributes> voxelAttributesMap   = new IntMap<>();
    private       int                     voxelCount           = 0;
    private       ChunkRenderer           chunkRenderer        = null;
    private       boolean                 graphicsInitialized  = false;
    private       boolean                 needsRenderingUpdate = false;

    public Chunk(@NotNull World world, int cx, int cz) {
        this.world = world;
        this.cx = cx;
        this.cz = cz;
    }

    public void fullyInitialize() {
        chunkRenderer = new ChunkRenderer(this);
        graphicsInitialized = true;
    }

    private void throwIfNotFullyInitialized() {
        if (isNotFullyInitialized()) {
            throw new IllegalStateException("Chunk needs to be fully initialized for this operation");
        }
    }

    /**
     * Gets the voxel at (lx,ly,lz).
     * <p>
     * TODO: Replace this with an alternative that returns a VoxelSpec instead of a byte voxel
     *
     * @param lx Local chunk x
     * @param ly Local chunk y
     * @param lz Local chunk z
     * @return Voxel
     */
    public Voxel getVoxelAt_LP(int lx, int ly, int lz) {
        return Voxels.getSpecForVoxel(voxels[calcVoxelArrayPosition_LP(lx, ly, lz)]);
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
        VoxelAttributes attrs = getVoxelAttrsAt_LP(lx, ly, lz);
        if (attrs == null) {
            attrs = new VoxelAttributes();
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
        voxelAttributesMap.put(pos, voxelAttributes);
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
        if (removedValue == null && !ignoreNonexisting) {
            throw new IllegalStateException("No attribute for voxel (%d, %d, %d)".formatted(lx, ly, lz));
        }
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
     * @param voxel Voxel to put
     */
    public void putVoxelAt_LP(int lx, int ly, int lz, Voxel voxel) {
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
    public void putVoxelAt_LP(int lx, int ly, int lz, Voxel voxel, boolean keepAttributes) {
        Voxel prevVoxel = getVoxelAt_LP(lx, ly, lz);
        if (prevVoxel != null && prevVoxel != Voxels.AIR) {
            voxelCount++;
        } else if (prevVoxel != Voxels.AIR && voxel == Voxels.AIR) {
            voxelCount--;
            if (!keepAttributes) {
                delVoxelAttrsAt_LP(lx, ly, lz, true);
            }
        }
        voxels[calcVoxelArrayPosition_LP(lx, ly, lz)] = Voxels.getVoxelId(voxel);
        needsRenderingUpdate = true;
    }

    /**
     * Updates all voxels in this chunk.
     */
    public void updateVoxels() {
        for (int lx = 0; lx < CHUNK_SIZE; lx++) {
            for (int wy = 0; wy < CHUNK_DEPTH; wy++) {
                for (int lz = 0; lz < CHUNK_SIZE; lz++) {
                    Voxel v = getVoxelAt_LP(lx, wy, lz);
                    if (v == null || v == Voxels.AIR) {
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
            Voxel v = getVoxelAt_LP(lx, wy, lz);
            if (!Voxels.isAirOrNull(v)) {
                return wy;
            }
        }
        return -1;
    }

    /**
     * Slopifies every voxel in this chunk if they need to become slopes.
     */
    public void slopifyVoxels(boolean propagateToSurroundingChunks) {
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
        Voxel voxel = getVoxelAt_LP(lx, y, lz);
        if (Voxels.isAirOrNull(voxel) || !voxel.isCanBeSloped()) {
            return;
        }

        int wx = LxWx(cx, lx);
        int wz = LzWz(cz, lz);

        if (y < CHUNK_DEPTH - 1) {
            Voxel voxelAbove = getWorld().getVoxelAt_WP(wx, y + 1, wz);
            assert voxelAbove != null;
            if (voxelAbove != Voxels.AIR && !voxelAbove.isDestroyedBySloping()) {
                return;
            }
        }

        boolean isVoxelBelow = y > 0 && getWorld().getVoxelAt_WP(wx, y - 1, wz) != Voxels.AIR;
        if (!isVoxelBelow) {
            return;
        }

        @Nullable Voxel voxelWest = world.getVoxelAt_WP(wx - 1, y, wz);
        @Nullable Voxel voxelEast = world.getVoxelAt_WP(wx + 1, y, wz);
        @Nullable Voxel voxelSouth = world.getVoxelAt_WP(wx, y, wz + 1);
        @Nullable Voxel voxelNorth = world.getVoxelAt_WP(wx, y, wz - 1);
        @Nullable Voxel voxelNorthwest = world.getVoxelAt_WP(wx - 1, y, wz - 1);
        @Nullable Voxel voxelNortheast = world.getVoxelAt_WP(wx + 1, y, wz - 1);
        @Nullable Voxel voxelSouthwest = world.getVoxelAt_WP(wx - 1, y, wz + 1);
        @Nullable Voxel voxelSoutheast = world.getVoxelAt_WP(wx + 1, y, wz + 1);
        
        /*
          Southwest-facing corner
          ▢ ▢
          ▢ ◢
         */
        if (!Voxels.canVoxelConnectWithSlopes(voxelNorthwest)
                && !Voxels.canVoxelConnectWithSlopes(voxelWest)
                && !Voxels.canVoxelConnectWithSlopes(voxelNorth)) {
            slopifyVoxel(lx, y, lz, Direction.NORTHWEST, false);
        }
        /*
          Northeast-facing corner
          ▢ ▢
          ◣ ▢
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelNorth)
                && !Voxels.canVoxelConnectWithSlopes(voxelNortheast)
                && !Voxels.canVoxelConnectWithSlopes(voxelEast)) {
            slopifyVoxel(lx, y, lz, Direction.NORTHEAST, false);
        }
        /*
          Southwest-facing corner
          ▢ ◥
          ▢ ▢
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelSouthwest)
                && !Voxels.canVoxelConnectWithSlopes(voxelWest)
                && !Voxels.canVoxelConnectWithSlopes(voxelSouth)) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHWEST, false);
        }
        /*
          Southeast-facing corner
          ◤ ▢
          ▢ ▢
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelSoutheast)
                && !Voxels.canVoxelConnectWithSlopes(voxelEast)
                && !Voxels.canVoxelConnectWithSlopes(voxelSouth)) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHEAST, false);
        }
        /*
          Northwest-facing inner corner
          ▢ |
          _ 」←
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelNorthwest)
                && Voxels.canVoxelConnectWithSlopes(voxelWest)
                && Voxels.canVoxelConnectWithSlopes(voxelNorth)) {
            slopifyVoxel(lx, y, lz, Direction.NORTHWEST, true);
        }
        /*
         Northeast-facing inner corner
           | ▢
         → ⌞ _
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelNortheast)
                && Voxels.canVoxelConnectWithSlopes(voxelEast)
                && Voxels.canVoxelConnectWithSlopes(voxelNorth)) {
            slopifyVoxel(lx, y, lz, Direction.NORTHEAST, true);
        }
        /*
         Southwest-facing inner corner
            ̅ ⌝ ←
           ▢ |
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelSouthwest)
                && Voxels.canVoxelConnectWithSlopes(voxelWest)
                && Voxels.canVoxelConnectWithSlopes(voxelSouth)) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHWEST, true);
        }
        /*
         Southeast-facing inner corner
          → ⌜  ̅
            | ▢
         */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelSoutheast)
                && Voxels.canVoxelConnectWithSlopes(voxelEast)
                && Voxels.canVoxelConnectWithSlopes(voxelSouth)) {
            slopifyVoxel(lx, y, lz, Direction.SOUTHEAST, true);
        }
        /*  West edge */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelWest)) {
            slopifyVoxel(lx, y, lz, Direction.WEST, false);
        }
        /* East edge */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelEast)) {
            slopifyVoxel(lx, y, lz, Direction.EAST, false);
        }
        /* South edge */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelSouth)) {
            slopifyVoxel(lx, y, lz, Direction.SOUTH, false);
        }
        /* North edge */
        else if (!Voxels.canVoxelConnectWithSlopes(voxelNorth)) {
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

    /**
     * Slopifies a specific voxel at local coordinates.
     *
     * @param lx             Chunk local x
     * @param y              Y
     * @param lz             Chunk local z
     * @param slopeDirection
     * @param isInnerCorner
     */
    private void slopifyVoxel(int lx, int y, int lz, byte slopeDirection, boolean isInnerCorner) {
        VoxelAttributes attrs = getVoxelAttrsAt_G_LP(lx, y, lz);
        attrs.configureSlope(slopeDirection, isInnerCorner);

        Voxel voxel = getVoxelAt_LP(lx, y, lz);
        if (voxel == Voxels.GRASS) {
            putVoxelAt_LP(lx, y, lz, Voxels.DIRT, true);
        }

        if (y < CHUNK_DEPTH - 1) {
            Voxel above = getVoxelAt_LP(lx, y + 1, lz);
            if (above != null && above.isDestroyedBySloping()) {
                putVoxelAt_LP(lx, y + 1, lz, Voxels.AIR, false);
            }
        }
    }

    @Deprecated
    public void forceRerender() {
        throwIfNotFullyInitialized();
        setNeedsRenderingUpdate(true);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "cx=" + cx +
                ", cz=" + cz +
                '}';
    }

    public World getWorld() {
        return world;
    }

    public int getCx() {
        return cx;
    }

    public int getCz() {
        return cz;
    }

    public ChunkRenderer getChunkRenderingStrategy() {
        throwIfNotFullyInitialized();
        return chunkRenderer;
    }

    public boolean isNotFullyInitialized() {
        return !graphicsInitialized;
    }

    public boolean needsRenderingUpdate() {
        return needsRenderingUpdate;
    }

    public void setNeedsRenderingUpdate(boolean needsRenderingUpdate) {
        this.needsRenderingUpdate = needsRenderingUpdate;
    }

    @Override
    public void dispose() {
        if (chunkRenderer != null) {
            chunkRenderer.dispose();
        }
    }
}
