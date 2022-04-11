package vc.andro.poketest.tile;

public enum TileType {
    GRASS("grass", true, true),
    WALL("wall-bottom-edge", false, false),
    WATER("water", true, false),
    SAND("sand", true, true);

    public final String defaultSpriteId;
    public final boolean isFlatTile;
    public final boolean canPlayerWalkOnIt;

    TileType(String spriteId, boolean isFlatTile, boolean canPlayerWalkOnIt) {
        defaultSpriteId = spriteId;
        this.isFlatTile = isFlatTile;
        this.canPlayerWalkOnIt = canPlayerWalkOnIt;
    }
}
