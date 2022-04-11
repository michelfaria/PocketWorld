package vc.andro.poketest.tile;

public enum WallType {
    TOP_LEFT_CORNER("wall-top-left-corner"),
    TOP_EDGE("wall-top-edge"),
    TOP_RIGHT_CORNER("wall-top-right-corner"),
    LEFT_EDGE("wall-left-edge"),
    RIGHT_EDGE("wall-right-edge"),
    BOTTOM_LEFT_CORNER("wall-bottom-left-corner"),
    BOTTOM_EDGE("wall-bottom-edge"),
    BOTTOM_RIGHT_CORNER("wall-bottom-right-corner"),
    TOP_LEFT_INNER_CORNER("wall-top-left-inner-corner"),
    TOP_RIGHT_INNER_CORNER("wall-top-right-inner-corner"),
    BOTTOM_LEFT_INNER_CORNER("wall-bottom-left-inner-corner"),
    BOTTOM_RIGHT_INNER_CORNER("wall-bottom-right-inner-corner");

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
