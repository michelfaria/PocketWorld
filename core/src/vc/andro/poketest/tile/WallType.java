package vc.andro.poketest.tile;

public enum WallType {
    NORTHWEST_CORNER("tile/wall-top-left-corner"),
    NORTH_EDGE("tile/wall-top-edge"),
    NORTHEAST_CORNER("tile/wall-top-right-corner"),
    WEST_EDGE("tile/wall-left-edge"),
    EAST_EDGE("tile/wall-right-edge"),
    SOUTHWEST_EDGE("tile/wall-bottom-left-corner"),
    SOUTH_EDGE("tile/wall-bottom-edge"),
    SOUTHEAST_EDGE("tile/wall-bottom-right-corner"),
    NORTHWEST_INNER_CORNER("tile/wall-top-left-inner-corner"),
    NORTHEAST_INNER_CORNER("tile/wall-top-right-inner-corner"),
    SOUTHWEST_INNER_CORNER("tile/wall-bottom-left-inner-corner"),
    SOUTHEAST_INNER_CORNER("tile/wall-bottom-right-inner-corner");

    public final String spriteId;

    WallType(String spriteId) {
        this.spriteId = spriteId;
    }

}
