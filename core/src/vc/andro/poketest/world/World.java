package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.registry.RenderSettingsRegistry;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.VoxelSpec;
import vc.andro.poketest.voxel.VoxelSpecs;
import vc.andro.poketest.world.generation.WorldGenerator;

import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class World {
    private final WorldGenerator worldGenerator;
    private final CoordMat<Chunk> chunks;
    private final Array<Entity> entities;
    private final Array<WorldUpdateStep> updateSteps;
    private final Vector3 viewpointWp;

    public World(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
        chunks = new CoordMat<>();
        entities = new Array<>(Entity.class);
        updateSteps = new Array<>(WorldUpdateStep.class);
        viewpointWp = new Vector3();

        updateSteps.add(GenerateChunksInRenderDistanceWorldUpdateStep.getInstance());
        updateSteps.add(UnloadChunksWorldUpdateStep.getInstance());
        updateSteps.add(UnloadEntitiesWorldUpdateStep.getInstance());
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    public void update(float delta) {
        for (WorldUpdateStep step : updateSteps) {
            step.update(this, delta);
        }
    }

    public void updateChunk(int cx, int cz) {
        Chunk chunk = getChunkAt_CP(cx, cz);
        if (chunk == null) {
            throw new NullPointerException("No chunk at (%d, %d)".formatted(cx, cz));
        }
        chunk.updateVoxels();
    }

    public void putVoxelAt_G_WP(int wx, int y, int wz, byte voxel) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
        chunk.putVoxelAt_LP(WxLx(wx), y, WzLz(wz), voxel);
    }

    public byte getVoxelAt_WP(int wx, int y, int wz) {
        Chunk chunk = getChunkAt_CP(WxCx(wx), WzCz(wz));
        if (chunk == null) {
            return -1;
        }
        return chunk.getVoxelAt_LP(WxLx(wx), y, WzLz(wz));
    }

    private Chunk getChunkAt_G_WP(int wx, int wz) {
        return getChunkAt_G_CP(WxCx(wx), WzCz(wz));
    }

    public Chunk createBlankChunkAt_CP(int cx, int cz) {
        if (getChunkAt_CP(cx, cz) != null) {
            throw new IllegalArgumentException("A chunk already exists at %d,%d".formatted(cx, cz));
        }
        Chunk blankChunk = Chunk.POOL.obtain();
        blankChunk.init(this, cx, cz);
        chunks.set(cx, cz, blankChunk);
        return blankChunk;
    }

    public @Nullable
    Chunk getChunkAt_WP(int wx, int wz) {
        return getChunkAt_CP(
                WxCx(wx),
                WzCz(wz));
    }

    public Chunk getChunkAt_G_CP(int cx, int cz) {
        Chunk chunk = getChunkAt_CP(cx, cz);
        if (chunk == null) {
            worldGenerator.generateChunk(cx, cz);
            chunk = getChunkAt_CP(cx, cz);
            assert chunk != null : "chunk should have generated";
        }
        return chunk;
    }

    public @Nullable
    Chunk getChunkAt_CP(int cx, int cz) {
        return chunks.get(cx, cz);
    }

    public int getSurfaceVoxelWy_WP(int wx, int wz) {
        Chunk chunk = getChunkAt_CP(
                WxCx(wx),
                WzCz(wz));
        if (chunk == null) {
            return -1;
        }
        return chunk.getSurfaceVoxelWy_LP(
                WxLx(wx),
                WzLz(wz));
    }

    public int getSurfaceVoxelWy_G_WP(int wx, int wz) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
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

    public CoordMat<Chunk> getChunks() {
        return chunks;
    }

    Array<Entity> getEntities() {
        return entities;
    }

    public void unloadChunk(Chunk chunk) {
        if (chunks.remove(chunk.cx, chunk.cz) == null) {
            throw new IllegalStateException("failed to remove chunk from chunk map");
        }
        Gdx.app.log("World", "Unloaded chunk at (" + chunk.cx + ", " + chunk.cz + ")!");
        Chunk.POOL.free(chunk);
    }

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
        if (spec.transparent) {
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

    public @Nullable Chunk getChunkAtViewpoint() {
        int cx = WxCx(viewpointWp.x);
        int cz = WzCz(viewpointWp.z);
        return getChunkAt_CP(cx, cz);
    }

    public boolean isChunkOutsideOfRenderDistance(Chunk chunk) {
        return isChunkOutsideOfRenderDistance_CP(chunk.cx, chunk.cz);
    }

    public boolean isChunkOutsideOfRenderDistance_CP(int cx, int cz) {
        return Math.abs(WxCx(viewpointWp.x) - cx) > RenderSettingsRegistry.renderDistance
                || Math.abs(WzCz(viewpointWp.z) - cz) > RenderSettingsRegistry.renderDistance;
    }

    public void removeEntity(Entity e) {
        if (!entities.removeValue(e, true)) {
            throw new IllegalArgumentException("Entity does not exist in world");
        }
    }

    public void generateChunkAtViewpoint() {
        int cx = WxCx(viewpointWp.x);
        int cz = WzCz(viewpointWp.z);
        getChunkAt_G_CP(cx, cz);
    }
}
