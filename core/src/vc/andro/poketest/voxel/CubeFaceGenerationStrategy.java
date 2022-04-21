package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.VertexArray;

import static vc.andro.poketest.voxel.VoxelTypes.VOXEL_TYPES;

public class CubeFaceGenerationStrategy implements FaceGenerationStrategy {

    private static CubeFaceGenerationStrategy INSTANCE;

    private CubeFaceGenerationStrategy() {
    }

    public static synchronized CubeFaceGenerationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CubeFaceGenerationStrategy();
        }
        return INSTANCE;
    }

    @Override
    public void createTopVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {

        vertices.addVertex8f(
                wx,
                wy + 1,
                wz,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getU(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getV(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        vertices.addVertex8f(
                wx + 1,
                wy + 1,
                wz,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getU2(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getV(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        vertices.addVertex8f(
                wx + 1,
                wy + 1,
                wz + 1,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getU2(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getV2(CubicGroup.Face.TOP, voxel, wx, wy, wz)
        );

        vertices.addVertex8f(
                wx,
                wy + 1,
                wz + 1,
                0,
                1,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getU(CubicGroup.Face.TOP, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.top.getV2(CubicGroup.Face.TOP, voxel, wx, wy, wz)
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
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getU(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getV(CubicGroup.Face.EAST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getU2(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getV(CubicGroup.Face.EAST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + 1,
                wz + 1,
                1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getU2(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getV2(CubicGroup.Face.EAST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + 1,
                wz,
                1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getU(CubicGroup.Face.EAST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.east.getV2(CubicGroup.Face.EAST, voxel, wx, wy, wz));
    }

    @Override
    public void createNorthVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                0,
                0,
                1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getU(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getV(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz,
                0,
                0,
                1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getU2(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getV(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + 1,
                wz,
                0,
                0,
                1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getU2(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getV2(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + 1,
                wz,
                0,
                0,
                1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getU(CubicGroup.Face.NORTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.north.getV2(CubicGroup.Face.NORTH, voxel, wx, wy, wz));
    }

    @Override
    public void createSouthVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                0,
                0,
                -1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getU(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getV(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + 1,
                wz + 1,
                0,
                0,
                -1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getU(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getV2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy + 1,
                wz + 1,
                0,
                0,
                -1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getU2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getV2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx + 1,
                wy,
                wz + 1,
                0,
                0,
                -1,
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getU2(CubicGroup.Face.SOUTH, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.south.getV(CubicGroup.Face.SOUTH, voxel, wx, wy, wz));
    }

    @Override
    public void createBottomVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
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
    }

    @Override
    public void createWestVertices(VertexArray vertices, byte voxel, int wx, int wy, int wz) {
        vertices.addVertex8f(
                wx,
                wy,
                wz,
                -1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getU(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getV(CubicGroup.Face.WEST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + 1,
                wz,
                -1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getU(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getV2(CubicGroup.Face.WEST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy + 1,
                wz + 1,
                -1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getU2(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getV2(CubicGroup.Face.WEST, voxel, wx, wy, wz));
        vertices.addVertex8f(
                wx,
                wy,
                wz + 1,
                -1,
                0,
                0,
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getU2(CubicGroup.Face.WEST, voxel, wx, wy, wz),
                VOXEL_TYPES[voxel].uvCalculationStrategies.west.getV(CubicGroup.Face.WEST, voxel, wx, wy, wz));
    }
}
