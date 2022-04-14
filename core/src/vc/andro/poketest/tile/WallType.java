package vc.andro.poketest.tile;

public enum WallType {
    TOP_LEFT_CORNER("tile/wall-top-left-corner"),
    TOP_EDGE("tile/wall-top-edge"),
    TOP_RIGHT_CORNER("tile/wall-top-right-corner"),
    LEFT_EDGE("tile/wall-left-edge"),
    RIGHT_EDGE("tile/wall-right-edge"),
    BOTTOM_LEFT_CORNER("tile/wall-bottom-left-corner"),
    BOTTOM_EDGE("tile/wall-bottom-edge"),
    BOTTOM_RIGHT_CORNER("tile/wall-bottom-right-corner"),
    TOP_LEFT_INNER_CORNER("tile/wall-top-left-inner-corner"),
    TOP_RIGHT_INNER_CORNER("tile/wall-top-right-inner-corner"),
    BOTTOM_LEFT_INNER_CORNER("tile/wall-bottom-left-inner-corner"),
    BOTTOM_RIGHT_INNER_CORNER("tile/wall-bottom-right-inner-corner");

    private final String spriteId;

    WallType(String spriteId) {
        this.spriteId = spriteId;
    }

    public String getSpriteId() {
        if (spriteId == null) {
            throw new UnsupportedOperationException("This wall type is currently unsupported");
        }
        return spriteId;
    }
}
