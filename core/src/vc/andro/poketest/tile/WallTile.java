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

    @Override
    public void createTopVertices(VertexArray vertices) {
        // southwest
        vertices.addVertex(
                worldX,                    // x
                y + 1,                     // y
                worldZ,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV2()      // v
        );
        // southeast
        vertices.addVertex(
                worldX + 1,                // x
                y + 1,                     // y
                worldZ,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV2()      // v
        );
        // northeast
        vertices.addVertex(
                worldX + 1,                // x
                y + 1,                     // y
                worldZ + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV()       // v
        );
        // northwest
        vertices.addVertex(
                worldX,                    // x
                y + 1,                     // y
                worldZ + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV()       // v
        );
    }

    @Override
    public void createLeftVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,        // x
                y,             // y
                worldZ,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y + 1,         // y
                worldZ,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y + 1,         // y
                worldZ + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y,             // y
                worldZ + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
    }
}
