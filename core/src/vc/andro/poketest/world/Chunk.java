package vc.andro.poketest.world;

import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.tile.BasicTile;

public class Chunk {

    public static final int CHUNK_SIZE = 16; // in tiles
    public static final int CHUNK_DEPTH = 128;

    public final World world;
    public final int chunkX;
    public final int chunkZ;
    public final BasicTile[][][] tiles;

    public Chunk(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        tiles = new BasicTile[CHUNK_SIZE][CHUNK_DEPTH][CHUNK_SIZE];
    }

    public @Nullable BasicTile getTileAt(int chunkLocalX, int y, int chunkLocalZ) {
        return tiles[chunkLocalX][y][chunkLocalZ];
    }

    public void putTileAt(int chunkLocalX, int y, int chunkLocalZ, BasicTile tile) {
        tile.world = world;
        tile.chunk = this;
        tile.worldX = chunkX * CHUNK_SIZE + chunkLocalX;
        tile.worldZ = chunkZ * CHUNK_SIZE + chunkLocalZ;
        tile.y = y;
        tile.chunkLocalX = chunkLocalX;
        tile.chunkLocalZ = chunkLocalZ;
        tiles[chunkLocalX][y][chunkLocalZ] = tile;
    }

    public void updateTiles() {
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int y = 0; y < CHUNK_DEPTH; y++) {
                for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                    BasicTile tile = getTileAt(chunkLocalX, y, chunkLocalZ);
                    if (tile == null) {
                        continue;
                    }
                    tile.doTileUpdate();
                }
            }
        }
    }

    public @Nullable BasicTile getSurfaceTile(int localChunkX, int localChunkZ) {
        for (int y = CHUNK_DEPTH - 1; y >= 0; y--) {
            BasicTile tile = getTileAt(localChunkX, y, localChunkZ);
            if (tile != null) {
                return tile;
            }
        }
        return null;
    }
}
