package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicTile;

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

    public void updateChunk(int chunkX, int chunkY) {
        Chunk chunk = getChunkAtPos(chunkX, chunkY);
        if (chunk == null) {
            throw new NullPointerException("no such chunk (" + chunkX + ", " + chunkY + ")");
        }
        chunk.updateTiles();
    }

    public void broadcastTileUpdateToAdjacentTiles(BasicTile updateOrigin) {
        final int ox = updateOrigin.worldX;
        final int oy = updateOrigin.worldY;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (Math.abs(dx) == Math.abs(dy)) {
                    continue;
                }
                BasicTile tile = getTileAt(ox + dx, oy + dy);
                if (tile != null) {
                    tile.receiveTileUpdate(updateOrigin);
                }
            }
        }
    }

    public @NotNull
    BasicTile putTileAt(int worldX, int worldY, @NotNull BasicTile tile) {
        final int chunkX = worldXToChunkX(worldX);
        final int chunkY = worldYToChunkY(worldY);
        final Chunk chunk = getChunkOrCreateNew(chunkX, chunkY);
        final int chunkLocalX = worldXToChunkLocalX(worldX);
        final int chunkLocalY = worldYToChunkLocalY(worldY);
        chunk.putTileAt(chunkLocalX, chunkLocalY, tile);
        return tile;
    }

    @Nullable
    public BasicTile getTileAt(int worldX, int worldY) {
        final int chunkX = worldXToChunkX(worldX);
        final int chunkY = worldYToChunkY(worldY);
        Chunk chunk = getChunkAtPos(chunkX, chunkY);
        if (chunk == null) {
            return null;
        }
        final int chunkLocalX = worldXToChunkLocalX(worldX);
        final int chunkLocalY = worldYToChunkLocalY(worldY);
        BasicTile tile = chunk.getTileAt(chunkLocalX, chunkLocalY);
        if (tile == null) {
            throw new IllegalStateException("No tile at world x:" + worldX + ", y:" + worldY);
        }
        return tile;
    }

    public BasicTile getTileOrGenerateAt(int worldX, int worldY) {
        final int chunkX = worldXToChunkX(worldX);
        final int chunkY = worldYToChunkY(worldY);
        Chunk chunk = getChunkAtPos(chunkX, chunkY);
        if (chunk == null) {
            worldGenerator.generateChunk(chunkX, chunkY);
            chunk = getChunkAtPos(chunkX, chunkY);
            assert chunk != null : "chunk should have generated";
        }
        final int chunkLocalX = worldXToChunkLocalX(worldX);
        final int chunkLocalY = worldYToChunkLocalY(worldY);
        BasicTile tile = chunk.getTileAt(chunkLocalX, chunkLocalY);
        if (tile == null) {
            throw new IllegalStateException("No tile at world x:" + worldX + ", y:" + worldY);
        }
        return tile;
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    private @Nullable
    Chunk getChunkAtPos(int chunkX, int chunkY) {
        return chunks.get(chunkX, chunkY);
    }

    private Chunk getChunkOrCreateNew(int chunkX, int chunkY) {
        Chunk chunk = chunks.get(chunkX, chunkY);
        if (chunk == null) {
            chunk = new Chunk(this, chunkX, chunkY);
            chunks.set(chunkX, chunkY, chunk);
        }
        return chunk;
    }

    public static int worldXToChunkX(int worldX) {
        return (int) Math.floor(worldX / (float) CHUNK_SIZE);
    }

    public static int worldYToChunkY(int worldY) {
        return (int) Math.floor(worldY / (float) CHUNK_SIZE);
    }

    public static int chunkXToWorldX(int chunkX) {
        return chunkX * CHUNK_SIZE;
    }

    public static int chunkYToWorldY(int chunkY) {
        return chunkY * CHUNK_SIZE;
    }

    public static int worldXToChunkLocalX(int worldX) {
        if (worldX >= 0) {
            return worldX % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (worldX % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int worldYToChunkLocalY(int worldY) {
        if (worldY >= 0) {
            return worldY % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (worldY % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int chunkLocalXToWorldX(int chunkX, int localChunkX) {
        return chunkX * CHUNK_SIZE + localChunkX;
    }

    public static int chunkLocalYToWorldY(int chunkY, int localChunkY) {
        return chunkY * CHUNK_SIZE + localChunkY;
    }
}
