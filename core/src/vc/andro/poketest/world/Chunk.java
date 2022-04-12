package vc.andro.poketest.world;

import vc.andro.poketest.tile.BasicTile;

public class Chunk {

    public static final int CHUNK_SIZE = 16; // in tiles

    public final World world;
    public final int chunkX;
    public final int chunkY;
    public final BasicTile[][] tiles;

    public Chunk(World world, int chunkX, int chunkY) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        tiles = new BasicTile[CHUNK_SIZE][CHUNK_SIZE];
    }

    public BasicTile getTileAt(int chunkLocalX, int chunkLocalY) {
        return tiles[chunkLocalX][chunkLocalY];
    }

    public BasicTile putTileAt(int chunkLocalX, int chunkLocalY, BasicTile tile) {
        final BasicTile prev = getTileAt(chunkLocalX, chunkLocalY);
        tile.world = world;
        tile.chunk = this;
        tile.worldX = chunkX * CHUNK_SIZE + chunkLocalX;
        tile.worldY = chunkY * CHUNK_SIZE + chunkLocalY;
        tile.chunkLocalX = chunkLocalX;
        tile.chunkLocalY = chunkLocalY;
        tiles[chunkLocalX][chunkLocalY] = tile;
        return prev;
    }

    public void updateTiles() {
        for (int chunkLocalX = 0; chunkLocalX < CHUNK_SIZE; chunkLocalX++) {
            for (int chunkLocalY = 0; chunkLocalY < CHUNK_SIZE; chunkLocalY++) {
                BasicTile tile = getTileAt(chunkLocalX, chunkLocalY);
                assert tile != null; // No null tiles should exist in a chunk
                tile.doTileUpdate();
            }
        }
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "chunkX=" + chunkX +
                ", chunkY=" + chunkY +
                '}';
    }
}
