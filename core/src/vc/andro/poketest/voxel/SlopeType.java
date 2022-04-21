package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public enum SlopeType {
    NORTHWEST_CORNER("tile/wall-top-left-corner"),
    NORTH_EDGE("tile/wall-top-edge"),
    NORTHEAST_CORNER("tile/wall-top-right-corner"),
    WEST_EDGE("tile/wall-left-edge"),
    EAST_EDGE("tile/wall-right-edge"),
    SOUTHWEST_CORNER("tile/wall-bottom-left-corner"),
    SOUTH_EDGE("tile/wall-bottom-edge"),
    SOUTHEAST_CORNER("tile/wall-bottom-right-corner"),
    NORTHWEST_INNER_CORNER("tile/wall-top-left-inner-corner"),
    NORTHEAST_INNER_CORNER("tile/wall-top-right-inner-corner"),
    SOUTHWEST_INNER_CORNER("tile/wall-bottom-left-inner-corner"),
    SOUTHEAST_INNER_CORNER("tile/wall-bottom-right-inner-corner");

    public final CubicGroup<String> spriteIds;

    SlopeType(String topFaceSpriteId) {
        spriteIds = new CubicGroup<>(topFaceSpriteId, null, null, null, null, null);
    }

}
