package vc.andro.poketest.tile;

public enum VoxelType {
    GRASS("tile/grass", true),
    SLOPE("tile/wall-bottom-edge", false),
    WATER("tile/water", false),
    SAND("tile/sand", true);

    public final String defaultSpriteId;
    public final boolean canPlayerWalkOnIt;

    VoxelType(String spriteId, boolean canPlayerWalkOnIt) {
        defaultSpriteId = spriteId;
        this.canPlayerWalkOnIt = canPlayerWalkOnIt;
    }
}
