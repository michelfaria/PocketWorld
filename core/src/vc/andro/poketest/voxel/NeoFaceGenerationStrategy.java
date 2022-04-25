package vc.andro.poketest.voxel;

import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Direction;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;

import static vc.andro.poketest.Direction.*;
import static vc.andro.poketest.voxel.VoxelSpecs.VOXEL_TYPES;

public class NeoFaceGenerationStrategy implements FaceGenerationStrategy {

    private static volatile NeoFaceGenerationStrategy sInstance;

    private NeoFaceGenerationStrategy() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + NeoFaceGenerationStrategy.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static NeoFaceGenerationStrategy getInstance() {
        if (sInstance == null) {
            synchronized (NeoFaceGenerationStrategy.class) {
                if (sInstance == null) {
                    sInstance = new NeoFaceGenerationStrategy();
                }
            }
        }
        return sInstance;
    }

    @SuppressWarnings("DuplicatedCode")
    private float getHeightInDirection(byte direction, @Nullable VoxelAttributes voxelAttributes) {
        if (voxelAttributes == null || voxelAttributes.getSlopeFacingDirection() == Direction.NA) {
            return 1.0f;
        }

        boolean isInnerCorner = voxelAttributes.getIsInnerCornerSlope();

        switch (voxelAttributes.getSlopeFacingDirection()) {
            case NORTHWEST -> {
                if (isInnerCorner) {
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
                } else {
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
            }
            case NORTHEAST -> {
                if (isInnerCorner) {
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
                } else {
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
            }
            case SOUTHWEST -> {
                if (isInnerCorner) {
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
                } else {
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
            }
            case SOUTHEAST -> {
                if (isInnerCorner) {
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
                } else {
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
            }
            case NORTH -> {
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
            case SOUTH -> {
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
            case WEST -> {
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
            case EAST -> {
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
            default -> throw new AssertionError();
        }
    }

    @Override
    public void createTopVertices(VertexArray vertices, IndexArray indices, byte voxel, @Nullable
            VoxelAttributes attributes, int wx, int wy, int wz) {
        UVCalculationStrategy strat = VOXEL_TYPES[voxel].uvCalculationStrategies.getFace(CubicGroup.Face.TOP);
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(NORTHWEST, attributes),
                wz,
                0,
                1,
                0,
                strat.getU(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(NORTHEAST, attributes),
                wz,
                0,
                1,
                0,
                strat.getU2(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(SOUTHEAST, attributes),
                wz + 1,
                0,
                1,
                0,
                strat.getU2(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(SOUTHWEST, attributes),
                wz + 1,
                0,
                1,
                0,
                strat.getU(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        indices.addSquare();
    }

    @Override
    public void createEastVertices(VertexArray vertices, IndexArray indices, byte voxel,
                                   @Nullable VoxelAttributes attributes, int wx, int wy, int wz) {
        UVCalculationStrategy strat = VOXEL_TYPES[voxel].uvCalculationStrategies.getFace(CubicGroup.Face.EAST);
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz,
                1,
                0,
                0,
                strat.getU(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.EAST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                1,
                0,
                0,
                strat.getU2(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.EAST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(SOUTHEAST, attributes),
                wz + 1,
                1,
                0,
                0,
                strat.getU2(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.EAST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(NORTHEAST, attributes),
                wz,
                1,
                0,
                0,
                strat.getU(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.EAST, voxel, wx, wy, wz));

        indices.addSquare();
    }

    @Override
    public void createNorthVertices(VertexArray vertices, IndexArray indices, byte voxel,
                                    @Nullable VoxelAttributes attributes, int wx, int wy, int wz) {
        UVCalculationStrategy strat = VOXEL_TYPES[voxel].uvCalculationStrategies.getFace(CubicGroup.Face.NORTH);
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                0,
                0,
                1,
                strat.getU(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz,
                0,
                0,
                1,
                strat.getU2(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(NORTHEAST, attributes),
                wz,
                0,
                0,
                1,
                strat.getU2(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(NORTHWEST, attributes),
                wz,
                0,
                0,
                1,
                strat.getU(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.NORTH, voxel, wx, wy, wz));

        indices.addSquare();
    }

    @Override
    public void createSouthVertices(VertexArray vertices, IndexArray indices, byte voxel,
                                    @Nullable VoxelAttributes attributes, int wx, int wy, int wz) {
        UVCalculationStrategy strat = VOXEL_TYPES[voxel].uvCalculationStrategies.getFace(CubicGroup.Face.SOUTH);
        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                0,
                0,
                -1,
                strat.getU(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(SOUTHWEST, attributes),
                wz + 1,
                0,
                0,
                -1,
                strat.getU(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + getHeightInDirection(SOUTHEAST, attributes),
                wz + 1,
                0,
                0,
                -1,
                strat.getU2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                0,
                0,
                -1,
                strat.getU2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));

        indices.addSquare();
    }

    @Override
    public void createWestVertices(VertexArray vertices, IndexArray indices, byte voxel,
                                   @Nullable VoxelAttributes attributes, int wx, int wy, int wz) {
        UVCalculationStrategy strat = VOXEL_TYPES[voxel].uvCalculationStrategies.getFace(CubicGroup.Face.WEST);
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                -1,
                0,
                0,
                strat.getU(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.WEST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(NORTHWEST, attributes),
                wz,
                -1,
                0,
                0,
                strat.getU(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.WEST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + getHeightInDirection(SOUTHWEST, attributes),
                wz + 1,
                -1,
                0,
                0,
                strat.getU2(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                strat.getV2(CubicGroup.Face.WEST, voxel, wx, wy, wz));

        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                -1,
                0,
                0,
                strat.getU2(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                strat.getV(CubicGroup.Face.WEST, voxel, wx, wy, wz));

        indices.addSquare();
    }

    @Override
    public void createBottomVertices(VertexArray vertices, IndexArray indices, byte voxel,
                                     @Nullable VoxelAttributes attributes, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                0,
                -1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getU(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getV(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                0,
                -1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getU(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getV2(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                0,
                -1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getU2(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getV2(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz,
                0,
                -1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getU2(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.bottom.getV(CubicGroup.Face.BOTTOM, voxel, wx, wy, wz));

        indices.addSquare();
    }
}
