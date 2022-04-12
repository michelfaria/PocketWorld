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

    public void updateChunk(int chunkX, int chunkZ) {
        Chunk chunk = getChunkAtPos(chunkX, chunkZ);
        if (chunk == null) {
            throw new NullPointerException("no such chunk (" + chunkX + ", " + chunkZ + ")");
        }
        chunk.updateTiles();
    }

    public void broadcastTileUpdateToAdjacentTiles(BasicTile updateOrigin) {
        final int ox = updateOrigin.worldX;
        final int oz = updateOrigin.worldZ;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (Math.abs(dx) == Math.abs(dz)) {
                    continue;
                }
                BasicTile tile = getTileAt(ox + dx, oz + dz);
                if (tile != null) {
                    tile.receiveTileUpdate(updateOrigin);
                }
            }
        }
    }

    public @NotNull
    BasicTile putTileAt(int worldX, int worldZ, @NotNull BasicTile tile) {
        final int chunkX = worldXToChunkX(worldX);
        final int chunkZ = worldZTochunkZ(worldZ);
        final Chunk chunk = getChunkOrCreateNew(chunkX, chunkZ);
        final int chunkLocalX = worldXToChunkLocalX(worldX);
        final int chunkLocalZ = worldYTochunkLocalZ(worldZ);
        chunk.putTileAt(chunkLocalX, chunkLocalZ, tile);
        return tile;
    }

    @Nullable
    public BasicTile getTileAt(int worldX, int worldZ) {
        final int chunkX = worldXToChunkX(worldX);
        final int chunkZ = worldZTochunkZ(worldZ);
        Chunk chunk = getChunkAtPos(chunkX, chunkZ);
        if (chunk == null) {
            return null;
        }
        final int chunkLocalX = worldXToChunkLocalX(worldX);
        final int chunkLocalZ = worldYTochunkLocalZ(worldZ);
        BasicTile tile = chunk.getTileAt(chunkLocalX, chunkLocalZ);
        if (tile == null) {
            throw new IllegalStateException("No tile at world x:" + worldX + ", z:" + worldZ);
        }
        return tile;
    }

    public BasicTile getTileOrGenerateAt(int worldX, int worldZ) {
        final int chunkX = worldXToChunkX(worldX);
        final int chunkZ = worldZTochunkZ(worldZ);
        Chunk chunk = getChunkAtPos(chunkX, chunkZ);
        if (chunk == null) {
            worldGenerator.generateChunk(chunkX, chunkZ);
            chunk = getChunkAtPos(chunkX, chunkZ);
            assert chunk != null : "chunk should have generated";
        }
        final int chunkLocalX = worldXToChunkLocalX(worldX);
        final int chunkLocalZ = worldYTochunkLocalZ(worldZ);
        BasicTile tile = chunk.getTileAt(chunkLocalX, chunkLocalZ);
        if (tile == null) {
            throw new IllegalStateException("No tile at world x:" + worldX + ", z:" + worldZ);
        }
        return tile;
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    private @Nullable
    Chunk getChunkAtPos(int chunkX, int chunkZ) {
        return chunks.get(chunkX, chunkZ);
    }

    private Chunk getChunkOrCreateNew(int chunkX, int chunkZ) {
        Chunk chunk = chunks.get(chunkX, chunkZ);
        if (chunk == null) {
            chunk = new Chunk(this, chunkX, chunkZ);
            chunks.set(chunkX, chunkZ, chunk);
        }
        return chunk;
    }

    public static int worldXToChunkX(int worldX) {
        return (int) Math.floor(worldX / (float) CHUNK_SIZE);
    }

    public static int worldZTochunkZ(int worldZ) {
        return (int) Math.floor(worldZ / (float) CHUNK_SIZE);
    }

    public static int chunkXToWorldX(int chunkX) {
        return chunkX * CHUNK_SIZE;
    }

    public static int chunkZToWorldZ(int chunkZ) {
        return chunkZ * CHUNK_SIZE;
    }

    public static int worldXToChunkLocalX(int worldX) {
        if (worldX >= 0) {
            return worldX % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (worldX % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int worldYTochunkLocalZ(int worldZ) {
        if (worldZ >= 0) {
            return worldZ % CHUNK_SIZE;
        }
        return (CHUNK_SIZE + (worldZ % CHUNK_SIZE)) % CHUNK_SIZE;
    }

    public static int chunkLocalXToWorldX(int chunkX, int localChunkX) {
        return chunkX * CHUNK_SIZE + localChunkX;
    }

    public static int chunkLocalZToWorldZ(int chunkZ, int localchunkZ) {
        return chunkZ * CHUNK_SIZE + localchunkZ;
    }
}
