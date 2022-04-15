package vc.andro.poketest.tile;

import vc.andro.poketest.Direction;
import vc.andro.poketest.world.VertexArray;

public class WallTile extends BasicTile {

    private WallType wallType;

    public WallTile(WallType wallType) {
        super(TileType.WALL);
        this.wallType = wallType;
        updateWallType(wallType);
        transparent = true;
    }

    public void updateWallType(WallType newType) {
        wallType = newType;
        setSprite(newType.spriteId);
    }

    private float getHeightInDirection(Direction direction) {
        switch (wallType) {
            case NORTHWEST_CORNER -> {
                switch (direction) {
                    case NORTHWEST, NORTHEAST, SOUTHEAST -> {
                        return 0;
                    }
                    case WEST, NORTH, EAST, SOUTH -> {
                        return 0.5f;
                    }
                    case SOUTHWEST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }

            case NORTH_EDGE, NORTHEAST_CORNER, WEST_EDGE, SOUTHEAST_INNER_CORNER, SOUTHWEST_INNER_CORNER, NORTHEAST_INNER_CORNER, NORTHWEST_INNER_CORNER, SOUTHEAST_EDGE, SOUTH_EDGE, SOUTHWEST_EDGE, EAST_EDGE -> {
                return 1;// todo
            }
            default -> throw new AssertionError();
        }
    }
}
