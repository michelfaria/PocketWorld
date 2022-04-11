package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicTile;

public class World {
    private final WorldBase base;
    private final CoordMat<Chunk> chunks;
    private final Array<Entity> entities;

    public World(WorldBase base) {
        this.base = base;
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

    public WorldBase getBase() {
        return base;
    }

    public int getHeight() {
        return base.getHeight();
    }

    public int getWidth() {
        return base.getWidth();
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

    public void updateAllTiles() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                getTileAt(x, y).doTileUpdate();
            }
        }
    }

    public void propagateTileUpdate(BasicTile updateOrigin) {
        final int ox = updateOrigin.worldX;
        final int oy = updateOrigin.worldY;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == dy || ox + dx < 0 || ox + dx >= getWidth() || oy + dy < 0 || oy + dy >= getHeight()) {
                    continue;
                }
                BasicTile tile = getTileAt(ox + dx, oy + dy);
                tile.receiveTileUpdate(updateOrigin);
            }
        }
    }

    public BasicTile putTileAt(int worldX, int worldY, BasicTile tile) {
        final ChunkPos pos = calculateChunkPosition(worldX, worldY);
        final Chunk chunk = getChunkOrCreateNew(pos.chunkX, pos.chunkY);
        chunk.putTileAt(pos.chunkLocalX, pos.chunkLocalY, tile);
        return tile;
    }

    public BasicTile getTileAt(int worldX, int worldY) {
        final ChunkPos pos = calculateChunkPosition(worldX, worldY);
        final Chunk chunk = getChunkOrThrowError(pos.chunkX, pos.chunkY);
        return chunk.getTileAt(pos.chunkLocalX, pos.chunkLocalY);
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    private Chunk getChunkOrThrowError(int chunkX, int chunkY) {
        Chunk chunk = chunks.get(chunkX, chunkY);
        if (chunk == null) {
            throw new IllegalArgumentException("chunk (%d,%d) not found".formatted(chunkX, chunkY));
        }
        return chunk;
    }

    private Chunk getChunkOrCreateNew(int chunkX, int chunkY) {
        Chunk chunk = chunks.get(chunkX, chunkY);
        if (chunk == null) {
            chunk = new Chunk(this, chunkX, chunkY);
            chunks.set(chunkX, chunkY, chunk);
        }
        return chunk;
    }

    private ChunkPos calculateChunkPosition(int worldX, int worldY) {
        return new ChunkPos(
                worldX / Chunk.CHUNK_SIZE,
                worldY / Chunk.CHUNK_SIZE,
                worldX % Chunk.CHUNK_SIZE,
                worldY % Chunk.CHUNK_SIZE
        );
    }

    private record ChunkPos(int chunkX, int chunkY, int chunkLocalX, int chunkLocalY) {
    }
}
