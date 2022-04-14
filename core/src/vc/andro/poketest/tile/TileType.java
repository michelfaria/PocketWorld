package vc.andro.poketest.tile;

public enum TileType {
    GRASS("tile/grass", true, true),
    WALL("tile/wall-bottom-edge", false, false),
    WATER("tile/water", true, false),
    SAND("tile/sand", true, true);

    public final String defaultSpriteId;
    public final boolean isFlatTile;
    public final boolean canPlayerWalkOnIt;

    TileType(String spriteId, boolean isFlatTile, boolean canPlayerWalkOnIt) {
        defaultSpriteId = spriteId;
        this.isFlatTile = isFlatTile;
        this.canPlayerWalkOnIt = canPlayerWalkOnIt;
    }
}
