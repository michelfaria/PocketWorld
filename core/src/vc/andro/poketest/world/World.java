package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.PocketCamera;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.world.generation.WorldGenerator;

import java.util.Iterator;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class World implements RenderableProvider {
    private final WorldCreationParams creationParams;
    private final WorldGenerator worldGenerator;
    private final CoordMat<Chunk> chunks;
    private final Array<Entity> entities;

    private int rx;
    private int rz;
    public int renderDistance = 10;

    private int dbgChunksRendered;
    private int dbgEntitiesRendered;

    public World(WorldCreationParams creationParams, WorldGenerator worldGenerator) {
        this.creationParams = creationParams;
        this.worldGenerator = worldGenerator;
        chunks = new CoordMat<>();
        entities = new Array<>(Entity.class);
    }

    public void addEntity(Entity e) {
        this.entities.add(e);
    }

    public void addEntities(Array<Entity> entities) {
        for (Entity entity : entities) {
            this.entities.add(entity);
        }
    }

    public WorldCreationParams getCreationParams() {
        return creationParams;
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
        int oy = updateOrigin.y;
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
        return chunk.getTileAt(
                WxLx(wx),
                y,
                WzLz(wz)
        );
    }

    public BasicVoxel getTileAt_G_WP(int wx, int y, int wz) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
        BasicVoxel tile = chunk.getTileAt(
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

    public Array<Entity> getEntities() {
        return entities;
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
    BasicVoxel getSurfaceTile(int wx, int wz) {
        Chunk chunk = getChunkAt_CP(
                WxCx(wx),
                WzCz(wz)
        );
        if (chunk == null) {
            return null;
        }
        return chunk.getSurfaceTile(
                WxLx(wx),
                WzLz(wz)
        );
    }

    public @Nullable
    BasicVoxel getSurfaceTile_G(int wx, int wz) {
        Chunk chunk = getChunkAt_G_WP(wx, wz);
        return chunk.getSurfaceTile(WxLx(wx), WzLz(wz));
    }

    public static int WxCx(int wx) {
        return (int) Math.floor(wx / (float) CHUNK_SIZE);
    }

    public static int WzCz(int wz) {
        return (int) Math.floor(wz / (float) CHUNK_SIZE);
    }

    public static int CxWx(int cx) {
        return cx * CHUNK_SIZE;
    }

    public static int CzWz(int cz) {
        return cz * CHUNK_SIZE;
    }

    public static int WxLx(int wx) {
        if (wx >= 0) {
            return wx % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (wx % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int WzLz(int wz) {
        if (wz >= 0) {
            return wz % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (wz % CHUNK_SIZE)) % CHUNK_SIZE;
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
        int cx = WxCx(rx);
        int cz = WzCz(rz);
        for (int ix = cx - renderDistance; ix < cx + renderDistance; ix++) {
            for (int iz = cz - renderDistance; iz < cz + renderDistance; iz++) {
                Chunk chunk = getChunkAt_G_CP(ix, iz);
                chunk.getRenderables(renderables, pool);
                dbgChunksRendered++;
            }
        }
    }

    public void renderEntities(DecalBatch decalBatch, PocketCamera pocketCamera) {
        dbgEntitiesRendered = 0;
        for (Entity entity : entities) {
            entity.draw(decalBatch, pocketCamera);
            dbgEntitiesRendered++;
        }
    }

    public void setRenderPosition(int renderX, int renderZ) {
        unloadChunksOutsideOfRenderDistance(renderX, renderZ);
        this.rx = renderX;
        this.rz = renderZ;
    }

    private void unloadChunksOutsideOfRenderDistance(int renderX, int renderZ) {
        int cx = WxCx(renderX);
        int cz = WzCz(renderZ);
        for (IntMap<Chunk> yMap : chunks.map.values()) {
            Iterator<Chunk> iterChunk = yMap.values().iterator();
            while (iterChunk.hasNext()) {
                Chunk chunk = iterChunk.next();
                if (Math.abs(cx - chunk.cx) > renderDistance || Math.abs(cz - chunk.cz) > renderDistance) {
                    iterChunk.remove();
                    System.out.println("removed chunk at (" + chunk.cx + "," + chunk.cz + ")");
                }
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
