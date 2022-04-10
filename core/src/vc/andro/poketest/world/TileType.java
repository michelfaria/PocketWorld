package vc.andro.poketest.world;

public enum TileType {
    GRASS("tile.grass", "grass"),
    WALL("tile.wall", "wall-bottom"),
    WATER("tile.water", "water"),
    SAND("tile.sand", "sand"),
    SLOPE("tile.slope", "grass");

    public final String typeId;
    public final String spriteId;

    TileType(String typeId, String spriteId) {
        this.typeId = typeId;
        this.spriteId = spriteId;
    }
}
