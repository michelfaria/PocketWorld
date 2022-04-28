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
import vc.andro.poketest.world.chunk.Chunk;
import vc.andro.poketest.world.chunk.ChunkMatrix;
import vc.andro.poketest.world.generation.WorldGenerator;

import static vc.andro.poketest.world.chunk.Chunk.CHUNK_SIZE;

public class World {
    private final WorldGenerator     worldGenerator;
    private final ChunkMatrix<Chunk> chunks   = new ChunkMatrix<>();
    private final Array<Entity>      entities = new Array<>(Entity.class);
    private final Vector3         viewpointWp = new Vector3();

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
     * Adds an entity to this world.
     *
     * @param e Entity to add
     */
    public void addEntity(Entity e) {
        entities.add(e);
    }

    /**
     * Ticks all entities in this world.
     */
    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    /**
     * Updates this world.
     *
     * @param delta Delta
     */
    public synchronized void update(float delta) {
        for (WorldUpdateStep step : updateSteps) {
            step.update(this, delta);
        }
    }

    /**
     * Updates the chunk at the specified chunk coordinates.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void updateChunk(int cx, int cz) {
        Chunk chunk = getChunkAt_CP(cx, cz);
        if (chunk == null) {
            throw new NullPointerException("No chunk at (%d, %d)".formatted(cx, cz));
        }
        chunk.updateVoxels();
    }

    /**
     * Gets a voxel at a world coordinate.
     *
     * @param wx World X
     * @param y  World Y
     * @param wz World Z
     * @return The voxel, or -1 if it doesn't exist.
     */
    public byte getVoxelAt_WP(int wx, int y, int wz) {
        Chunk chunk = getChunkAt_CP(WxCx(wx), WzCz(wz));
        if (chunk == null) {
            return -1;
        }
        return chunk.getVoxelAt_LP(WxLx(wx), y, WzLz(wz));
    }

    /**
     * Gets a voxel at a world coordinate.
     *
     * @param wx World X
     * @param y  World Y
     * @param wz World Z
     * @return The voxel spec, or -1 if it doesn't exist.
     */
    @Nullable
    public VoxelSpec getVoxelSpecAt_WP(int wx, int y, int wz) {
        byte voxel = getVoxelAt_WP(wx, y, wz);
        if (voxel < 0) {
            return null;
        }
        return VoxelSpecs.VOXEL_TYPES[voxel];
    }

    /**
     * Creates a blank chunk at the specified world coordinates.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     * @return The blank chunk
     */
    @NotNull
    public Chunk putEmptyChunkAt_CP(int cx, int cz) {
        if (getChunkAt_CP(cx, cz) != null) {
            throw new IllegalArgumentException("A chunk already exists at %d,%d".formatted(cx, cz));
        }
        Chunk chunk = Chunk.POOL.obtain();
        chunk.init(this, cx, cz);
        chunks.set(cx, cz, chunk);
        return chunk;
    }

    /**
     * Gets a chunk at the specified world coordinates.
     *
     * @param wx World X
     * @param wz World Z
     * @return The chunk if it exists, or null.
     */
    @Nullable
    public Chunk getChunkAt_WP(int wx, int wz) {
        return getChunkAt_CP(WxCx(wx), WzCz(wz));
    }

    /**
     * Generates a chunk at the specified chunk coordinates.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void generateChunkAt_CP(int cx, int cz) {
        worldGenerator.queueChunkForGeneration(cx, cz);
    }

    /**
     * Generates a chunk at the specified chunk coordinates if it doesn't exist.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     */
    public void generateChunkAt_CP_IfNotExists(int cx, int cz) {
        Chunk chunk = getChunkAt_CP(cx, cz);
        if (chunk == null) {
            generateChunkAt_CP(cx, cz);
        }
    }

    /**
     * Gets a Chunk at the specified chunk coordinates.
     *
     * @param cx Chunk X
     * @param cz Chunk Z
     * @return The chunk if it exists, or null.
     */
    @Nullable
    public Chunk getChunkAt_CP(int cx, int cz) {
        return chunks.get(cx, cz);
    }

    /**
     * Gets the Y coordinate of the top-most voxel at the specified world X, Z coordinates.
     *
     * @param wx World X
     * @param wz World Z
     * @return Y coordinate of the top-most voxel at the specified (X, Z) position
     */
    public int getSurfaceVoxelWy_WP(int wx, int wz) {
        Chunk chunk = getChunkAt_CP(WxCx(wx), WzCz(wz));
        if (chunk == null) {
            return -1;
        }
        return chunk.getSurfaceVoxelWy_LP(WxLx(wx), WzLz(wz));
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
     * Removes the specified chunk from this world.
     *
     * @param chunk Chunk to remove
     */
    public void unloadChunk(@NotNull Chunk chunk) {
        if (chunks.remove(chunk.getCx(), chunk.getCz()) == null) {
            throw new IllegalStateException("failed to remove chunk from chunk map");
        }
        Gdx.app.log("World", "Unloaded chunk at (" + chunk.getCx() + ", " + chunk.getCz() + ")!");
        Chunk.POOL.free(chunk);
    }

    /**
     * Returns true if the voxel at the specified world coordinates is effectively transparent.
     *
     * @param wx World X
     * @param y  World Y
     * @param wz World Z
     * @return true if the voxel at the specified world coordinates is effectively transparent
     */
    public boolean isVoxelAtPosEffectivelyTransparent_WP(int wx, int y, int wz) {
        Chunk chunk = getChunkAt_WP(wx, wz);
        if (chunk == null) {
            return false;
        }
        int lx = WxLx(wx);
        int lz = WzLz(wz);
        byte voxel = chunk.getVoxelAt_LP(lx, y, lz);
        if (voxel <= 0) {
            return true;
        }
        VoxelSpec spec = VoxelSpecs.VOXEL_TYPES[voxel];
        assert spec != null : "Voxel spec not found";
        if (spec.isTransparent()) {
            return true;
        }
        VoxelAttributes attrs = chunk.getVoxelAttrsAt_LP(lx, y, lz);
        if (attrs != null && attrs.isSlope()) {
            return true;
        }
        return false;
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
     *
     * @param chunk
     * @return
     */
    public boolean isChunkOutsideOfRenderDistance(Chunk chunk) {
        return isChunkOutsideOfRenderDistance_CP(chunk.getCx(), chunk.getCz());
    }

    /**
     * Returns true if the specified chunk coordinates are outside the render distance.
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
     * Removes an entity from this world.
     *
     * @param e Entity to remove
     */
    public void removeEntity(Entity e) {
        if (!entities.removeValue(e, true)) {
            throw new IllegalArgumentException("Entity does not exist in world");
        }
    }

    /**
     * Generates a chunk at the world viewpoint.
     */
    public void generateChunkAtViewpoint() {
        int cx = WxCx(viewpointWp.x);
        int cz = WzCz(viewpointWp.z);
        generateChunkAt_CP_IfNotExists(cx, cz);
    }

    /**
     * Returns the chunk matrix. <b>Always acquire the chunk lock before accessing or modifying the returned object!</b>
     *
     * @return
     */
    ChunkMatrix<Chunk> getChunks() {
        return chunks;
    }

    public synchronized void generateQueuedChunks() {
        worldGenerator.generateQueuedChunks();
    }
}
