package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.world.generation.WorldGenerator;

import java.util.Iterator;

import static vc.andro.poketest.PocketWorld.PPU;
import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class World implements RenderableProvider {
    private final WorldGenerator worldGenerator;
    private final CoordMat<Chunk> chunks;
    private final Array<Entity> entities;

    private float viewpointWx;
    private float viewpointWz;

    public int renderDistanceInChunks = 10;

    private int dbgChunksRendered;
    private int dbgEntitiesRendered;

    public World(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
        chunks = new CoordMat<>();
        entities = new Array<>(Entity.class);
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void addEntities(Array<Entity> entities) {
        for (Entity entity : entities) {
            this.entities.add(entity);
        }
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    public void update(float delta) {
        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    public void updateChunk(int cx, int cz) {
        Chunk chunk = getChunkAt_CP(cx, cz);
        if (chunk == null) {
            throw new NullPointerException("no such chunk (" + cx + ", " + cz + ")");
        }
        chunk.updateTiles();
    }

    private static final int[] ADJACENT_POSITIONS = new int[]{
            // x, y, z
            -1, 0, 0,
            0, -1, 0,
            0, 0, -1,
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public void broadcastTileUpdateToAdjacentTiles(BasicVoxel updateOrigin) {
        int ox = updateOrigin.wx;
        int oy = updateOrigin.wy;
        int oz = updateOrigin.wz;
        for (int i = 0; i < ADJACENT_POSITIONS.length; i += 3) {
            int dx = ADJACENT_POSITIONS[i];
            int dy = ADJACENT_POSITIONS[i + 1];
            int dz = ADJACENT_POSITIONS[i + 2];
            if (oy + dy >= CHUNK_DEPTH || oy - dy < 0) {
                continue;
            }
            BasicVoxel tile = getTileAt_WP(ox + dx, oy + dy, oz + dz);
            if (tile != null) {
                tile.receiveTileUpdate(updateOrigin);
            }
        }
    }

    public void putTileAt_WP(int wx, int y, int wz, @NotNull BasicVoxel tile) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
        chunk.putTileAt(
                WxLx(wx),
                y,
                WzLz(wz),
                tile
        );
    }

    @Nullable
    public BasicVoxel getTileAt_WP(int wx, int y, int wz) {
        Chunk chunk = getChunkAt_CP(
                WxCx(wx),
                WzCz(wz)
        );
        if (chunk == null) {
            return null;
        }
        return chunk.getTileAt_LP(
                WxLx(wx),
                y,
                WzLz(wz)
        );
    }

    public BasicVoxel getTileAt_G_WP(int wx, int y, int wz) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
        BasicVoxel tile = chunk.getTileAt_LP(
                WxLx(wx),
                y,
                WzLz(wz)
        );
        if (tile == null) {
            throw new IllegalStateException("No tile at position (%d, %d, %d)".formatted(wx, y, wz));
        }
        return tile;
    }

    private Chunk getChunkAt_G_WP(int wx, int wz) {
        return getChunkAt_G_CP(WxCx(wx), WzCz(wz));
    }

    private void createBlankChunkAt_CP(int cx, int cz) {
        if (getChunkAt_CP(cx, cz) != null) {
            throw new IllegalArgumentException("chunk already exists at %d,%d".formatted(cx, cz));
        }
        Chunk emptyChunk = new Chunk(this, cx, cz);
        chunks.set(cx, cz, emptyChunk);
    }

    public @Nullable
    Chunk getChunkAt_WP(int wx, int wz) {
        return getChunkAt_CP(
                WxCx(wx),
                WzLz(wz)
        );
    }

    public Chunk getChunkAt_G_CP(int cx, int cz) {
        Chunk chunk = getChunkAt_CP(cx, cz);
        if (chunk == null) {
            createBlankChunkAt_CP(cx, cz);
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

    public @Nullable
    BasicVoxel getSurfaceTile_WP(int wx, int wz) {
        Chunk chunk = getChunkAt_CP(
                WxCx(wx),
                WzCz(wz)
        );
        if (chunk == null) {
            return null;
        }
        return chunk.getSurfaceTile_LP(
                WxLx(wx),
                WzLz(wz)
        );
    }

    public @Nullable
    BasicVoxel getSurfaceTile_G_WP(int wx, int wz) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
        return chunk.getSurfaceTile_LP(WxLx(wx), WzLz(wz));
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
        return wx >= 0
                ? wx % CHUNK_SIZE
                : (CHUNK_SIZE + (wx % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int WzLz(int wz) {
        return wz >= 0
                ? wz % CHUNK_SIZE
                : (CHUNK_SIZE + (wz % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int LxWx(int cx, int lx) {
        return cx * CHUNK_SIZE + lx;
    }

    public static int LzWz(int cz, int lz) {
        return cz * CHUNK_SIZE + lz;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        dbgChunksRendered = 0;
        int cx = WxCx(viewpointWx);
        int cz = WzCz(viewpointWz);
        for (int ix = cx - renderDistanceInChunks; ix < cx + renderDistanceInChunks; ix++) {
            for (int iz = cz - renderDistanceInChunks; iz < cz + renderDistanceInChunks; iz++) {
                Chunk chunk = getChunkAt_G_CP(ix, iz);
                chunk.getRenderables(renderables, pool);
                dbgChunksRendered++;
            }
        }
    }

    public void renderEntities(DecalBatch decalBatch) {
        dbgEntitiesRendered = 0;
        for (Entity entity : entities) {
            entity.draw(decalBatch);
            dbgEntitiesRendered++;
        }
    }

    public void setCameraPosition_RP(float renderX, float renderZ) {
        viewpointWx = renderX;
        viewpointWz = renderZ;
        unloadChunksOutsideOfRenderDistance();
        deleteEntitiesOutsideOfRenderDistance();
    }

    private void unloadChunksOutsideOfRenderDistance() {
        for (IntMap<Chunk> yMap : chunks.map.values()) {
            Iterator<Chunk> iterChunk = yMap.values().iterator();
            while (iterChunk.hasNext()) {
                Chunk chunk = iterChunk.next();
                if (isChunkOutsideOfRenderDistance(chunk)) {
                    iterChunk.remove();
                    Gdx.app.log("World", "DELETED chunk at (" + chunk.cx + "," + chunk.cz + ")");
                }
            }
        }
    }

    private boolean isChunkOutsideOfRenderDistance(Chunk chunk) {
        return isChunkOutsideOfRenderDistance(chunk.cx, chunk.cz);
    }

    private boolean isChunkOutsideOfRenderDistance(int cx, int cz) {
        return Math.abs(WxCx(viewpointWx) - cx) > renderDistanceInChunks || Math.abs(WzCz(viewpointWz) - cz) > renderDistanceInChunks;
    }

    private void deleteEntitiesOutsideOfRenderDistance() {
        for (Array.ArrayIterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
            Entity entity = iterator.next();
            int entityCx = WxCx(entity.getWx());
            int entityCz = WzCz(entity.getWz());
            if (isChunkOutsideOfRenderDistance(entityCx, entityCz)) {
                iterator.remove();
                Gdx.app.log("World", "DELETED entity:" + entity);
            }
        }
    }

    public int getDbgChunksRendered() {
        return dbgChunksRendered;
    }

    public int getDbgEntitiesRendered() {
        return dbgEntitiesRendered;
    }
}
