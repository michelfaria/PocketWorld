package vc.andro.poketest.world;

import vc.andro.poketest.tile.BasicTile;

public class Chunk {

    public static final int CHUNK_SIZE = 16; // in tiles

    public final World world;
    public final int chunkX;
    public final int chunkZ;
    public final BasicTile[][] tiles;

    public Chunk(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        tiles = new BasicTile[CHUNK_SIZE][CHUNK_SIZE];
    }

    public BasicTile getTileAt(int chunkLocalX, int chunkLocalY) {
        return tiles[chunkLocalX][chunkLocalY];
    }

    public BasicTile putTileAt(int chunkLocalX, int chunkLocalZ, BasicTile tile) {
        final BasicTile prev = getTileAt(chunkLocalX, chunkLocalZ);
        tile.world = world;
        tile.chunk = this;
        tile.worldX = chunkX * CHUNK_SIZE + chunkLocalX;
        tile.worldZ = chunkZ * CHUNK_SIZE + chunkLocalZ;
        tile.chunkLocalX = chunkLocalX;
        tile.chunkLocalZ = chunkLocalZ;
        tiles[chunkLocalX][chunkLocalZ] = tile;
        return prev;
    }

    public void updateTiles() {
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalZ = 0; chunkLocalZ < CHUNK_SIZE; chunkLocalZ++) {
                BasicTile tile = getTileAt(chunkLocalX, chunkLocalZ);
                assert tile != null; // No null tiles should exist in a chunk
                tile.doTileUpdate();
            }
        }
    }
}
