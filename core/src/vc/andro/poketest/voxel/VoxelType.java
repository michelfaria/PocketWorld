package vc.andro.poketest.voxel;

public enum VoxelType {
    GRASS("tile/bwgrass", true),
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
