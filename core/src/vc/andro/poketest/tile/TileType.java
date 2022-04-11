package vc.andro.poketest.tile;

public enum TileType {
    GRASS("tile.grass", "grass", true),
    WALL("tile.wall", "wall-bottom-edge", false),
    WATER("tile.water", "water", true),
    SAND("tile.sand", "sand", true),
    SLOPE("tile.slope", "grass", true);

    public final String gameId;
    public final String defaultSpriteId;
    public final boolean isFloorTile;

    TileType(String gameId, String spriteId, boolean isFloorTile) {
        this.gameId = gameId;
        defaultSpriteId = spriteId;
        this.isFloorTile = isFloorTile;
    }
}
