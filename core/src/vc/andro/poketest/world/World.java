package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.registry.RenderSettingsRegistry;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.VoxelSpec;
import vc.andro.poketest.voxel.VoxelSpecs;
import vc.andro.poketest.world.generation.WorldGenerator;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class World {
    private final WorldGenerator         worldGenerator;
    private final CoordMat<Chunk>        chunks       = new CoordMat<>();
    private final Array<Entity>          entities     = new Array<>(Entity.class);
    private final Vector3                viewpointWp  = new Vector3();
    private final ReentrantReadWriteLock chunksLock   = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock entitiesLock = new ReentrantReadWriteLock();

    private final Array<WorldUpdateStep> updateSteps = new Array<>(WorldUpdateStep.class);

    {
        updateSteps.add(GenerateChunksInRenderDistanceWorldUpdateStep.getInstance());
        updateSteps.add(UnloadChunksWorldUpdateStep.getInstance());
        updateSteps.add(UnloadEntitiesWorldUpdateStep.getInstance());
    }

    public World(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
    }

    /**
     * Adds an entity to this world. This method is thread safe.
     *
     * @param e Entity to add
     */
    public void addEntity(Entity e) {
        entitiesLock.writeLock().lock();
        try {
            entities.add(e);
        } finally {
            entitiesLock.writeLock().unlock();
        }
    }

    /**
     * Ticks all entities in this world. This method is thread safe.
     */
    public void tick() {
        entitiesLock.readLock().lock();
        try {
            for (Entity entity : entities) {
                entity.tick();
            }
        } finally {
            entitiesLock.readLock().unlock();
        }
    }

    /**
     * Updates this world. This method is thread safe.
     *
     * @param delta Delta
     */
    public synchronized void update(float delta) {
        for (WorldUpdateStep step : updateSteps) {
            step.update(this, delta);
        }
    }

    /**
     * Updates the chunk at the specified chunk coordinates. This method is thread safe.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void updateChunk(int cx, int cz) {
        chunksLock.writeLock().lock();
        try {
            Chunk chunk = getChunkAt_CP(cx, cz);
            if (chunk == null) {
                throw new NullPointerException("No chunk at (%d, %d)".formatted(cx, cz));
            }
            chunk.updateVoxels();
        } finally {
            chunksLock.writeLock().unlock();
        }
    }

    /**
     * Gets a voxel at a world coordinate. This method is thread safe.
     *
     * @param wx World X
     * @param y  World Y
     * @param wz World Z
     * @return The voxel, or -1 if it doesn't exist.
     */
    public byte getVoxelAt_WP(int wx, int y, int wz) {
        chunksLock.readLock().lock();
        try {
            Chunk chunk = getChunkAt_CP(WxCx(wx), WzCz(wz));
            if (chunk == null) {
                return -1;
            }
            return chunk.getVoxelAt_LP(WxLx(wx), y, WzLz(wz));
        } finally {
            chunksLock.readLock().unlock();
        }
    }

    /**
     * Creates a blank chunk at the specified world coordinates. This method is thread safe.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     * @return The blank chunk
     */
    @NotNull
    public Chunk putEmptyChunkAt_CP(int cx, int cz) {
        chunksLock.writeLock().lock();
        try {
            if (getChunkAt_CP(cx, cz) != null) {
                throw new IllegalArgumentException("A chunk already exists at %d,%d".formatted(cx, cz));
            }
            Chunk chunk = Chunk.POOL.obtain();
            chunk.init(this, cx, cz);
            chunks.set(cx, cz, chunk);
            return chunk;
        } finally {
            chunksLock.writeLock().unlock();
        }
    }

    /**
     * Gets a chunk at the specified world coordinates. This method is thread safe.
     *
     * @param wx World X
     * @param wz World Z
     * @return The chunk if it exists, or null.
     */
    @Nullable
    public Chunk getChunkAt_WP(int wx, int wz) {
        chunksLock.readLock().lock();
        try {
            return getChunkAt_CP(WxCx(wx), WzCz(wz));
        } finally {
            chunksLock.readLock().unlock();
        }
    }

    /**
     * Generates a chunk at the specified chunk coordinates. This method is thread safe.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void generateChunkAt_CP(int cx, int cz) {
        worldGenerator.queueChunkForGeneration(cx, cz);
    }

    /**
     * Generates a chunk at the specified chunk coordinates if it doesn't exist. This method is thread safe.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void generateChunkAt_CP_IfNotExists(int cx, int cz) {
        chunksLock.writeLock().lock();
        try {
            Chunk chunk = getChunkAt_CP(cx, cz);
            if (chunk == null) {
                generateChunkAt_CP(cx, cz);
            }
        } finally {
            chunksLock.writeLock().unlock();
        }
    }

    /**
     * Gets a Chunk at the specified chunk coordinates. This method is thread safe.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     * @return The chunk if it exists, or null.
     */
    @Nullable
    public Chunk getChunkAt_CP(int cx, int cz) {
        chunksLock.readLock().lock();
        try {
            return chunks.get(cx, cz);
        } finally {
            chunksLock.readLock().unlock();
        }
    }

    /**
     * Gets the Y coordinate of the top-most voxel at the specified world X, Z coordinates. This method is thread safe.
     *
     * @param wx World X
     * @param wz World Z
     * @return Y coordinate of the top-most voxel at the specified (X, Z) position
     */
    public int getSurfaceVoxelWy_WP(int wx, int wz) {
        chunksLock.readLock().lock();
        try {
            Chunk chunk = getChunkAt_CP(WxCx(wx), WzCz(wz));
            if (chunk == null) {
                return -1;
            }
            return chunk.getSurfaceVoxelWy_LP(WxLx(wx), WzLz(wz));
        } finally {
            chunksLock.readLock().unlock();
        }
    }

    public static int WxCx(float wx) {
        return (int) Math.floor(wx / (float) CHUNK_SIZE);
    }

    public static int WzCz(float wz) {
        return (int) Math.floor(wz / (float) CHUNK_SIZE);
    }

    public static int CxWx(int cx) {
        return cx * CHUNK_SIZE;
    }

    public static int CzWz(int cz) {
        return cz * CHUNK_SIZE;
    }

    public static int WxLx(int wx) {
        return Math.floorMod(wx, CHUNK_SIZE);
    }

    public static int WzLz(int wz) {
        return Math.floorMod(wz, CHUNK_SIZE);
    }

    public static int LxWx(int cx, int lx) {
        return cx * CHUNK_SIZE + lx;
    }

    public static int LzWz(int cz, int lz) {
        return cz * CHUNK_SIZE + lz;
    }

    Array<Entity> getEntities() {
        return entities;
    }

    /**
     * Removes the specified chunk from this world. This method is thread safe.
     *
     * @param chunk Chunk to remove
     */
    public void unloadChunk(@NotNull Chunk chunk) {
        chunksLock.writeLock().lock();
        try {
            if (chunks.remove(chunk.getCx(), chunk.getCz()) == null) {
                throw new IllegalStateException("failed to remove chunk from chunk map");
            }
            Gdx.app.log("World", "Unloaded chunk at (" + chunk.getCx() + ", " + chunk.getCz() + ")!");
            Chunk.POOL.free(chunk);
        } finally {
            chunksLock.writeLock().unlock();
        }
    }

    /**
     * Returns true if the voxel at the specified world coordinates is effectively transparent.
     * This method is thread safe.
     *
     * @param wx World X
     * @param y  World Y
     * @param wz World Z
     * @return true if the voxel at the specified world coordinates is effectively transparent
     */
    public boolean isVoxelAtPosEffectivelyTransparent_WP(int wx, int y, int wz) {
        chunksLock.readLock().lock();
        try {
            Chunk chunk = getChunkAt_WP(wx, wz);
            if (chunk == null) {
                return false;
            }
            chunk.getLock().readLock().lock();
            try {
                int lx = WxLx(wx);
                int lz = WzLz(wz);
                byte voxel = chunk.getVoxelAt_LP(lx, y, lz);
                if (voxel <= 0) {
                    return true;
                }
                VoxelSpec spec = VoxelSpecs.VOXEL_TYPES[voxel];
                assert spec != null : "Voxel spec not found";
                if (spec.transparent) {
                    return true;
                }
                VoxelAttributes attrs = chunk.getVoxelAttrsAt_LP(lx, y, lz);
                if (attrs != null && attrs.isSlope()) {
                    return true;
                }
                return false;
            } finally {
                chunk.getLock().readLock().unlock();
            }
        } finally {
            chunksLock.readLock().unlock();
        }
    }

    public void setViewpoint(float wx, float wy, float wz) {
        viewpointWp.x = wx;
        viewpointWp.y = wy;
        viewpointWp.z = wz;
    }

    public Vector3 getViewpointWp() {
        return viewpointWp;
    }

    @Nullable
    public Chunk getChunkAtViewpoint() {
        int cx = WxCx(viewpointWp.x);
        int cz = WzCz(viewpointWp.z);
        return getChunkAt_CP(cx, cz);
    }

    /**
     * Returns true if the specified chunk is outside the render distance.
     * This method is thread safe.
     *
     * @param chunk
     * @return
     */
    public boolean isChunkOutsideOfRenderDistance(Chunk chunk) {
        chunk.getLock().readLock().lock();
        try {
            return isChunkOutsideOfRenderDistance_CP(chunk.getCx(), chunk.getCz());
        } finally {
            chunk.getLock().readLock().unlock();
        }
    }

    /**
     * Returns true if the specified chunk coordinates are outside the render distance.
     * This method is thread safe.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     * @return
     */
    public boolean isChunkOutsideOfRenderDistance_CP(int cx, int cz) {
        return Math.abs(WxCx(viewpointWp.x) - cx) > RenderSettingsRegistry.renderDistance
                || Math.abs(WzCz(viewpointWp.z) - cz) > RenderSettingsRegistry.renderDistance;
    }

    /**
     * Removes an entity from this world. This method is thread safe.
     *
     * @param e Entity to remove
     */
    public void removeEntity(Entity e) {
        entitiesLock.writeLock().lock();
        try {
            if (!entities.removeValue(e, true)) {
                throw new IllegalArgumentException("Entity does not exist in world");
            }
        } finally {
            entitiesLock.writeLock().unlock();
        }
    }

    /**
     * Generates a chunk at the world viewpoint. This method is thread safe.
     */
    public void generateChunkAtViewpoint() {
        chunksLock.writeLock().lock();
        try {
            int cx = WxCx(viewpointWp.x);
            int cz = WzCz(viewpointWp.z);
            generateChunkAt_CP_IfNotExists(cx, cz);
        } finally {
            chunksLock.writeLock().unlock();
        }
    }

    ReentrantReadWriteLock getChunksLock() {
        return chunksLock;
    }

    ReentrantReadWriteLock getEntitiesLock() {
        return entitiesLock;
    }

    /**
     * Returns the chunk matrix. <b>Always acquire the chunk lock before accessing or modifying the returned object!</b>
     *
     * @return
     */
    CoordMat<Chunk> getChunks() {
        return chunks;
    }

    public synchronized void generateQueuedChunks() {
        worldGenerator.generateQueuedChunks();
    }
}
