package vc.andro.poketest.tile;

public enum TileType {
    GRASS("tile.grass", "grass"),
    WALL("tile.wall", "wall-bottom-edge"),
    WATER("tile.water", "water"),
    SAND("tile.sand", "sand"),
    SLOPE("tile.slope", "grass");

    public final String gameId;
    public final String defaultSpriteId;

    TileType(String typeId, String spriteId) {
        this.gameId = typeId;
        defaultSpriteId = spriteId;
    }
}
