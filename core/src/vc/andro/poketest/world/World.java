package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.voxel.BasicVoxel;
import vc.andro.poketest.world.generation.WorldGenerator;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class World {
    private final WorldGenerator worldGenerator;
    private final CoordMat<Chunk> chunks;
    private final Array<Entity> entities;

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
        int ox = updateOrigin.getWx();
        int oy = updateOrigin.getWy();
        int oz = updateOrigin.getWz();
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

    CoordMat<Chunk> getChunks() {
        return chunks;
    }

    Array<Entity> getEntities() {
        return entities;
    }
}
