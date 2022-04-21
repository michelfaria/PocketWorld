package vc.andro.poketest.voxel;

import vc.andro.poketest.Direction;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.VertexArray;

public class SlopeVoxel extends Voxel {

    private SlopeType slopeType;

    SlopeVoxel() {
    }

    public void setup(SlopeType slopeType) {
        super.setup(VoxelType.SLOPE);
        this.slopeType = slopeType;
        updateSlopeType(slopeType);
        setTransparent(true);
    }

    @Override
    public void reset() {
        super.reset();
        slopeType = null;
    }

    public void updateSlopeType(SlopeType newType) {
        slopeType = newType;
        setTextures(newType.spriteIds);
    }

    @SuppressWarnings("DuplicatedCode")
    private float getHeightInDirection(Direction direction) {
        switch (slopeType) {
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
            case NORTHWEST_INNER_CORNER -> {
                switch (direction) {
                    case NORTHWEST -> {
                        return 0;
                    }
                    case NORTH, SOUTH, EAST, WEST -> {
                        return 0.5f;
                    }
                    case NORTHEAST, SOUTHEAST, SOUTHWEST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case NORTHEAST_INNER_CORNER -> {
                switch (direction) {
                    case NORTHEAST -> {
                        return 0;
                    }
                    case NORTH, SOUTH, EAST, WEST -> {
                        return 0.5f;
                    }
                    case NORTHWEST, SOUTHWEST, SOUTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case SOUTHWEST_INNER_CORNER -> {
                switch (direction) {
                    case SOUTHWEST -> {
                        return 0;
                    }
                    case NORTH, SOUTH, EAST, WEST -> {
                        return 0.5f;
                    }
                    case SOUTHEAST, NORTHEAST, NORTHWEST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            case SOUTHEAST_INNER_CORNER -> {
                switch (direction) {
                    case SOUTHEAST -> {
                        return 0;
                    }
                    case NORTH, SOUTH, EAST, WEST -> {
                        return 0.5f;
                    }
                    case SOUTHWEST, NORTHWEST, NORTHEAST -> {
                        return 1;
                    }
                    default -> throw new AssertionError();
                }
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public void createTopVertices(VertexArray vertices) {
        // northwest
        vertices.addVertex8f(
                getWx(),                    // x
                getWy() + getHeightInDirection(Direction.NORTHWEST),                     // y
                getWz(),                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTextureRegion(CubicGroup.Face.TOP).getU(),      // u
                getTextureRegion(CubicGroup.Face.TOP).getV()      // v
        );
        // northeast
        vertices.addVertex8f(
                getWx() + 1,                // x
                getWy() + getHeightInDirection(Direction.NORTHEAST),                     // y
                getWz(),                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTextureRegion(CubicGroup.Face.TOP).getU2(),     // u
                getTextureRegion(CubicGroup.Face.TOP).getV()      // v
        );
        // southeast
        vertices.addVertex8f(
                getWx() + 1,                // x
                getWy() + getHeightInDirection(Direction.SOUTHEAST),                     // y
                getWz() + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTextureRegion(CubicGroup.Face.TOP).getU2(),     // u
                getTextureRegion(CubicGroup.Face.TOP).getV2()       // v
        );
        // southwest
        vertices.addVertex8f(
                getWx(),                    // x
                getWy() + getHeightInDirection(Direction.SOUTHWEST),                     // y
                getWz() + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTextureRegion(CubicGroup.Face.TOP).getU(),      // u
                getTextureRegion(CubicGroup.Face.TOP).getV2()       // v
        );
    }

    @Override
    public void createEastVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz(),         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + getHeightInDirection(Direction.SOUTHEAST),          // y
                getWz() + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + getHeightInDirection(Direction.NORTHEAST),          // y
                getWz(),         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
    }

    @Override
    public void createNorthVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy() + getHeightInDirection(Direction.NORTHWEST),              // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + getHeightInDirection(Direction.NORTHEAST),              // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),          // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),          // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
    }

    @Override
    public void createSouthVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy() + getHeightInDirection(Direction.SOUTHWEST),              // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),          // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + getHeightInDirection(Direction.SOUTHEAST),          // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
    }

    @Override
    public void createWestVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),        // x
                getWy() + getHeightInDirection(Direction.NORTHWEST),             // y
                getWz(),        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy(),         // y
                getWz(),        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy() + getHeightInDirection(Direction.SOUTHWEST),         // y
                getWz() + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy(),             // y
                getWz() + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
    }
}
