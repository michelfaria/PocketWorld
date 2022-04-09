package vc.andro.poketest.world;

public enum TileType {
    GRASS("grass"),
    WALL("wall-bottom"),
    WATER("water"),
    SAND("sand"),
    SLOPE("grass");

    public final String spriteId;

    TileType(String spriteId) {
        this.spriteId = spriteId;
    }
}
