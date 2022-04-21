package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public enum VoxelType {
    GRASS("tile/bwgrass"),
    SLOPE("tile/wall-bottom-edge"),
    WATER("tile/water"),
    SAND("tile/sand");

    public final CubicGroup<String> textureRegionIds;

    VoxelType(String spriteId) {
        textureRegionIds = new CubicGroup<>(spriteId);
    }

    VoxelType(String topSpriteId, String northSpriteId, String southSpriteId, String westSpriteId, String eastSpriteId, String bottomSpriteId) {
        textureRegionIds = new CubicGroup<>(topSpriteId, bottomSpriteId, westSpriteId, eastSpriteId, northSpriteId, southSpriteId);
    }
}
