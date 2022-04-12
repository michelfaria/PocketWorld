package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicTile;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class World {
    private final WorldCreationParams creationParams;
    private final WorldGenerator worldGenerator;
    private final CoordMat<Chunk> chunks;
    private final Array<Entity> entities;

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

    public void updateChunk(int chunkX, int chunkZ) {
        Chunk chunk = getChunkAt_CP(chunkX, chunkZ);
        if (chunk == null) {
            throw new NullPointerException("no such chunk (" + chunkX + ", " + chunkZ + ")");
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

    public void broadcastTileUpdateToAdjacentTiles(BasicTile updateOrigin) {
        int ox = updateOrigin.worldX;
        int oy = updateOrigin.y;
        int oz = updateOrigin.worldZ;
        for (int i = 0; i < ADJACENT_POSITIONS.length; i += 3) {
            int dx = ADJACENT_POSITIONS[i];
            int dy = ADJACENT_POSITIONS[i + 1];
            int dz = ADJACENT_POSITIONS[i + 2];
            if (oy + dy >= CHUNK_DEPTH || oy - dy < 0) {
                continue;
            }
            BasicTile tile = getTileAt_WP(ox + dx, oy + dy, oz + dz);
            if (tile != null) {
                tile.receiveTileUpdate(updateOrigin);
            }
        }
    }

    public void putTileAt_WP(int worldX, int y, int worldZ, @NotNull BasicTile tile) {
        Chunk chunk = getChunkAt_G_WP(worldX, worldZ);
        chunk.putTileAt(
                WxLx(worldX),
                y,
                WzLz(worldZ),
                tile
        );
    }

    @Nullable
    public BasicTile getTileAt_WP(int worldX, int y, int worldZ) {
        Chunk chunk = getChunkAt_CP(
                WxCx(worldX),
                WzCz(worldZ)
        );
        if (chunk == null) {
            return null;
        }
        return chunk.getTileAt(
                WxLx(worldX),
                y,
                WzLz(worldZ)
        );
    }

    public BasicTile getTileAt_G_WP(int worldX, int y, int worldZ) {
        Chunk chunk = getChunkAt_G_WP(worldX, worldZ);
        BasicTile tile = chunk.getTileAt(
                WxLx(worldX),
                y,
                WzLz(worldZ)
        );
        if (tile == null) {
            throw new IllegalStateException("No tile at position (%d, %d, %d)".formatted(worldX, y, worldZ));
        }
        return tile;
    }

    @NotNull
    private Chunk getChunkAt_G_WP(int worldX, int worldZ) {
        int chunkX = WxCx(worldX);
        int chunkZ = WzCz(worldZ);
        Chunk chunk = getChunkAt_CP(chunkX, chunkZ);
        if (chunk == null) {
            createBlankChunkAt_CP(chunkX, chunkZ);
            worldGenerator.generateChunk(chunkX, chunkZ);
            chunk = getChunkAt_CP(chunkX, chunkZ);
            assert chunk != null : "chunk should have generated";
        }
        return chunk;
    }

    private void createBlankChunkAt_CP(int chunkX, int chunkZ) {
        if (getChunkAt_CP(chunkX, chunkZ) != null) {
            throw new IllegalArgumentException("chunk already exists at %d,%d".formatted(chunkX, chunkZ));
        }
        Chunk emptyChunk = new Chunk(this, chunkX, chunkZ);
        chunks.set(chunkX, chunkZ, emptyChunk);
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    public @Nullable
    Chunk getChunkAt_WP(int worldX, int worldZ) {
        return getChunkAt_CP(
                WxCx(worldX),
                WzLz(worldZ)
        );
    }

    public @Nullable
    Chunk getChunkAt_CP(int chunkX, int chunkZ) {
        return chunks.get(chunkX, chunkZ);
    }

    public @Nullable
    BasicTile getSurfaceTile(int worldX, int worldZ) {
        Chunk chunk = getChunkAt_CP(
                WxCx(worldX),
                WzCz(worldZ)
        );
        if (chunk == null) {
            return null;
        }
        return chunk.getSurfaceTile(
                WxLx(worldX),
                WzLz(worldZ)
        );
    }

    public @Nullable
    BasicTile getSurfaceTile_G(int worldX, int worldZ) {
        Chunk chunk = getChunkAt_G_WP(worldX, worldZ);
        return chunk.getSurfaceTile(WxLx(worldX), WzLz(worldZ));
    }

    public static int WxCx(int worldX) {
        return (int) Math.floor(worldX / (float) CHUNK_SIZE);
    }

    public static int WzCz(int worldZ) {
        return (int) Math.floor(worldZ / (float) CHUNK_SIZE);
    }

    public static int CxWx(int chunkX) {
        return chunkX * CHUNK_SIZE;
    }

    public static int CzWz(int chunkZ) {
        return chunkZ * CHUNK_SIZE;
    }

    public static int WxLx(int worldX) {
        if (worldX >= 0) {
            return worldX % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (worldX % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int WzLz(int worldZ) {
        if (worldZ >= 0) {
            return worldZ % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (worldZ % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int LxWx(int chunkX, int localChunkX) {
        return chunkX * CHUNK_SIZE + localChunkX;
    }

    public static int LzWz(int chunkZ, int localChunkZ) {
        return chunkZ * CHUNK_SIZE + localChunkZ;
    }
}
