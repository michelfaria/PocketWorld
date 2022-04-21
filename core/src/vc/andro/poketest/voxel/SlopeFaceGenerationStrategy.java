package vc.andro.poketest.voxel;

import vc.andro.poketest.Direction;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.VertexArray;

import java.util.Objects;

import static vc.andro.poketest.voxel.VoxelTypes.VOXEL_TYPES;

public class SlopeFaceGenerationStrategy implements FaceGenerationStrategy {

    private static SlopeFaceGenerationStrategy INSTANCE;

    public static synchronized SlopeFaceGenerationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SlopeFaceGenerationStrategy();
        }
        return INSTANCE;
    }

    private SlopeFaceGenerationStrategy() {
    }

    @SuppressWarnings("DuplicatedCode")
    private float getHeightInDirection(Direction direction, byte voxel) {
        SlopeType slopeType = Objects.requireNonNull(VOXEL_TYPES[voxel].slopeType, "Voxel isn't a slope");
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
    public void createTopVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {

        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(Direction.NORTHWEST, voxel),
                wz,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getU(),
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getV()
        );

        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(Direction.NORTHEAST, voxel),
                wz,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getU2(),
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getV()
        );

        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(Direction.SOUTHEAST, voxel),
                wz + 1,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getU2(),
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getV2()
        );

        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(Direction.SOUTHWEST, voxel),
                wz + 1,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getU(),
                VOXEL_TYPES[voxel].textureRegions.getFace(CubicGroup.Face.TOP).getV2()
        );
    }

    @Override
    public void createEastVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz,
                1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(Direction.SOUTHEAST, voxel),
                wz + 1,
                1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(Direction.NORTHEAST, voxel),
                wz,
                1,
                0,
                0,
                0,
                0);
    }

    @Override
    public void createNorthVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(Direction.NORTHWEST, voxel),
                wz,
                0,
                0,
                1,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(Direction.NORTHEAST, voxel),
                wz,
                0,
                0,
                1,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz,
                0,
                0,
                1,
                0,
                0);
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                0,
                0,
                1,
                0,
                0);
    }

    @Override
    public void createSouthVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(Direction.SOUTHWEST, voxel),
                wz + 1,
                0,
                0,
                -1,
                0,
                0);
        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                0,
                0,
                -1,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(Direction.SOUTHEAST, voxel),
                wz + 1,
                0,
                0,
                -1,
                0,
                0);
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                0,
                0,
                -1,
                0,
                0);
    }

    @Override
    public void createWestVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(Direction.NORTHWEST, voxel),
                wz,
                -1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                -1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(Direction.SOUTHWEST, voxel),
                wz + 1,
                -1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                -1,
                0,
                0,
                0,
                0);
    }

    @Override
    public void createBottomVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
    }
}
