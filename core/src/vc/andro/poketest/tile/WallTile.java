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

    @SuppressWarnings("DuplicatedCode")
    private float getHeightInDirection(Direction direction) {
        switch (wallType) {
            case NORTHWEST_CORNER -> {
                switch (direction) {
                    case NORTHWEST, NORTHEAST, SOUTHWEST -> {
                        return 0;
                    }
                    case WEST, NORTH, EAST, SOUTH -> {
                        return 0.5f;
                    }
                    case SOUTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case NORTHEAST_CORNER -> {
                switch (direction) {
                    case NORTHEAST, NORTHWEST, SOUTHEAST -> {
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
            case SOUTHWEST_CORNER -> {
                switch (direction) {
                    case SOUTHWEST, SOUTHEAST, NORTHWEST -> {
                        return 0;
                    }
                    case WEST, NORTH, EAST, SOUTH -> {
                        return 0.5f;
                    }
                    case NORTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case SOUTHEAST_CORNER -> {
                switch (direction) {
                    case SOUTHEAST, SOUTHWEST, NORTHEAST -> {
                        return 0;
                    }
                    case WEST, NORTH, EAST, SOUTH -> {
                        return 0.5f;
                    }
                    case NORTHWEST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case NORTH_EDGE -> {
                switch (direction) {
                    case NORTHWEST, NORTH, NORTHEAST -> {
                        return 0;
                    }
                    case WEST, EAST -> {
                        return 0.5f;
                    }
                    case SOUTHWEST, SOUTH, SOUTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case SOUTH_EDGE -> {
                switch (direction) {
                    case SOUTHWEST, SOUTH, SOUTHEAST -> {
                        return 0;
                    }
                    case WEST, EAST -> {
                        return 0.5f;
                    }
                    case NORTHWEST, NORTH, NORTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case WEST_EDGE -> {
                switch (direction) {
                    case NORTHWEST, WEST, SOUTHWEST -> {
                        return 0;
                    }
                    case NORTH, SOUTH -> {
                        return 0.5f;
                    }
                    case NORTHEAST, EAST, SOUTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case EAST_EDGE -> {
                switch (direction) {
                    case NORTHEAST, EAST, SOUTHEAST -> {
                        return 0;
                    }
                    case NORTH, SOUTH -> {
                        return 0.5f;
                    }
                    case NORTHWEST, WEST, SOUTHWEST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case SOUTHEAST_INNER_CORNER, SOUTHWEST_INNER_CORNER, NORTHEAST_INNER_CORNER, NORTHWEST_INNER_CORNER -> {
                return 1;// todo
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public void createTopVertices(VertexArray vertices) {
        // northwest
        vertices.addVertex(
                worldX,                    // x
                y + getHeightInDirection(Direction.NORTHWEST),                     // y
                worldZ,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV()      // v
        );
        // northeast
        vertices.addVertex(
                worldX + 1,                // x
                y + getHeightInDirection(Direction.NORTHEAST),                     // y
                worldZ,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV()      // v
        );
        // southeast
        vertices.addVertex(
                worldX + 1,                // x
                y + getHeightInDirection(Direction.SOUTHEAST),                     // y
                worldZ + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV2()       // v
        );
        // southwest
        vertices.addVertex(
                worldX,                    // x
                y + getHeightInDirection(Direction.SOUTHWEST),                     // y
                worldZ + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV2()       // v
        );
    }

    public void createRightVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX + 1,     // x
                y + getHeightInDirection(Direction.NORTHEAST),              // y
                worldZ,         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + getHeightInDirection(Direction.SOUTHEAST),              // y
                worldZ + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + getHeightInDirection(Direction.NORTHEAST),          // y
                worldZ,         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
    }

    @Override
    public void createFrontVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,         // x
                y + getHeightInDirection(Direction.NORTHWEST),              // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + getHeightInDirection(Direction.NORTHEAST),              // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,          // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX,         // x
                y,          // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
    }

    @Override
    public void createBackVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,         // x
                y + getHeightInDirection(Direction.SOUTHWEST),              // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX,         // x
                y,          // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + getHeightInDirection(Direction.SOUTHEAST),          // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
    }

    @Override
    public void createLeftVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,        // x
                y + getHeightInDirection(Direction.NORTHWEST),             // y
                worldZ,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y,         // y
                worldZ,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y + getHeightInDirection(Direction.SOUTHWEST),         // y
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
