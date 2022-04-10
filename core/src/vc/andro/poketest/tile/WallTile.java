package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WallTile extends Tile {

    private enum WallType {
        TOP_LEFT_CORNER(null),
        TOP_EDGE(null),
        TOP_RIGHT_CORNER(null),
        LEFT_EDGE("wall-left-edge"),
        RIGHT_EDGE("wall-right-edge"),
        BOTTOM_LEFT_CORNER("wall-bottom-left-corner"),
        BOTTOM_EDGE("wall-bottom-edge"),
        BOTTOM_RIGHT_CORNER("wall-bottom-right-corner"),
        TOP_LEFT_INNER_CORNER(null),
        TOP_RIGHT_INNER_CORNER(null),
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

    public WallTile(float altitude, int x, int y) {
        super(TileType.WALL, x, y, altitude);
    }

    public void updateWallType(WallType newType) {
        setSpriteId(newType.spriteId);
    }
}
