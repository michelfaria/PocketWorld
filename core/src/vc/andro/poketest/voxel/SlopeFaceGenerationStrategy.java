package vc.andro.poketest.voxel;

import vc.andro.poketest.Direction;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.VertexArray;

import java.util.Objects;

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
    private float getHeightInDirection(Voxel v, Direction direction) {
        SlopeType slopeType = Objects.requireNonNull(v.getType().slopeType, "Voxel isn't a slope");
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
    public void createTopVertices(Voxel v, VertexArray vertices) {

        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + getHeightInDirection(v, Direction.NORTHWEST),
                v.getWz(),
                0,
                1,
                0,
                v.getTextureRegion(CubicGroup.Face.TOP).getU(),
                v.getTextureRegion(CubicGroup.Face.TOP).getV()
        );

        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + getHeightInDirection(v, Direction.NORTHEAST),
                v.getWz(),
                0,
                1,
                0,
                v.getTextureRegion(CubicGroup.Face.TOP).getU2(),
                v.getTextureRegion(CubicGroup.Face.TOP).getV()
        );

        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + getHeightInDirection(v, Direction.SOUTHEAST),
                v.getWz() + 1,
                0,
                1,
                0,
                v.getTextureRegion(CubicGroup.Face.TOP).getU2(),
                v.getTextureRegion(CubicGroup.Face.TOP).getV2()
        );

        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + getHeightInDirection(v, Direction.SOUTHWEST),
                v.getWz() + 1,
                0,
                1,
                0,
                v.getTextureRegion(CubicGroup.Face.TOP).getU(),
                v.getTextureRegion(CubicGroup.Face.TOP).getV2()
        );
    }

    @Override
    public void createEastVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz(),
                1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz() + 1,
                1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + getHeightInDirection(v, Direction.SOUTHEAST),
                v.getWz() + 1,
                1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + getHeightInDirection(v, Direction.NORTHEAST),
                v.getWz(),
                1,
                0,
                0,
                0,
                0);
    }

    @Override
    public void createNorthVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + getHeightInDirection(v, Direction.NORTHWEST),
                v.getWz(),
                0,
                0,
                1,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + getHeightInDirection(v, Direction.NORTHEAST),
                v.getWz(),
                0,
                0,
                1,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz(),
                0,
                0,
                1,
                0,
                0);
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz(),
                0,
                0,
                1,
                0,
                0);
    }

    @Override
    public void createSouthVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + getHeightInDirection(v, Direction.SOUTHWEST),
                v.getWz() + 1,
                0,
                0,
                -1,
                0,
                0);
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz() + 1,
                0,
                0,
                -1,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + getHeightInDirection(v, Direction.SOUTHEAST),
                v.getWz() + 1,
                0,
                0,
                -1,
                0,
                0);
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz() + 1,
                0,
                0,
                -1,
                0,
                0);
    }

    @Override
    public void createWestVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + getHeightInDirection(v, Direction.NORTHWEST),
                v.getWz(),
                -1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz(),
                -1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + getHeightInDirection(v, Direction.SOUTHWEST),
                v.getWz() + 1,
                -1,
                0,
                0,
                0,
                0);
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz() + 1,
                -1,
                0,
                0,
                0,
                0);
    }

    @Override
    public void createBottomVertices(Voxel v, VertexArray vertices) {
    }
}
