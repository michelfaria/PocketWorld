package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.VertexArray;

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
    public void createTopVertices(Voxel v, VertexArray vertices) {

        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + 1,
                v.getWz(),
                0,
                1,
                0,
                v.getUvCalculationStrategies().top.getU(v, CubicGroup.Face.TOP),
                v.getUvCalculationStrategies().top.getV(v, CubicGroup.Face.TOP)
        );

        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + 1,
                v.getWz(),
                0,
                1,
                0,
                v.getUvCalculationStrategies().top.getU2(v, CubicGroup.Face.TOP),
                v.getUvCalculationStrategies().top.getV(v, CubicGroup.Face.TOP)
        );

        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + 1,
                v.getWz() + 1,
                0,
                1,
                0,
                v.getUvCalculationStrategies().top.getU2(v, CubicGroup.Face.TOP),
                v.getUvCalculationStrategies().top.getV2(v, CubicGroup.Face.TOP)
        );

        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + 1,
                v.getWz() + 1,
                0,
                1,
                0,
                v.getUvCalculationStrategies().top.getU(v, CubicGroup.Face.TOP),
                v.getUvCalculationStrategies().top.getV2(v, CubicGroup.Face.TOP)
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
                v.getUvCalculationStrategies().east.getU(v, CubicGroup.Face.EAST),
                v.getUvCalculationStrategies().east.getV(v, CubicGroup.Face.EAST));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz() + 1,
                1,
                0,
                0,
                v.getUvCalculationStrategies().east.getU2(v, CubicGroup.Face.EAST),
                v.getUvCalculationStrategies().east.getV(v, CubicGroup.Face.EAST));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + 1,
                v.getWz() + 1,
                1,
                0,
                0,
                v.getUvCalculationStrategies().east.getU2(v, CubicGroup.Face.EAST),
                v.getUvCalculationStrategies().east.getV2(v, CubicGroup.Face.EAST));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + 1,
                v.getWz(),
                1,
                0,
                0,
                v.getUvCalculationStrategies().east.getU(v, CubicGroup.Face.EAST),
                v.getUvCalculationStrategies().east.getV2(v, CubicGroup.Face.EAST));
    }

    @Override
    public void createNorthVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz(),
                0,
                0,
                1,
                v.getUvCalculationStrategies().north.getU(v, CubicGroup.Face.NORTH),
                v.getUvCalculationStrategies().north.getV(v, CubicGroup.Face.NORTH));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz(),
                0,
                0,
                1,
                v.getUvCalculationStrategies().north.getU2(v, CubicGroup.Face.NORTH),
                v.getUvCalculationStrategies().north.getV(v, CubicGroup.Face.NORTH));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + 1,
                v.getWz(),
                0,
                0,
                1,
                v.getUvCalculationStrategies().north.getU2(v, CubicGroup.Face.NORTH),
                v.getUvCalculationStrategies().north.getV2(v, CubicGroup.Face.NORTH));
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + 1,
                v.getWz(),
                0,
                0,
                1,
                v.getUvCalculationStrategies().north.getU(v, CubicGroup.Face.NORTH),
                v.getUvCalculationStrategies().north.getV2(v, CubicGroup.Face.NORTH));
    }

    @Override
    public void createSouthVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz() + 1,
                0,
                0,
                -1,
                v.getUvCalculationStrategies().south.getU(v, CubicGroup.Face.SOUTH),
                v.getUvCalculationStrategies().south.getV(v, CubicGroup.Face.SOUTH));
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + 1,
                v.getWz() + 1,
                0,
                0,
                -1,
                v.getUvCalculationStrategies().south.getU(v, CubicGroup.Face.SOUTH),
                v.getUvCalculationStrategies().south.getV2(v, CubicGroup.Face.SOUTH));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy() + 1,
                v.getWz() + 1,
                0,
                0,
                -1,
                v.getUvCalculationStrategies().south.getU2(v, CubicGroup.Face.SOUTH),
                v.getUvCalculationStrategies().south.getV2(v, CubicGroup.Face.SOUTH));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz() + 1,
                0,
                0,
                -1,
                v.getUvCalculationStrategies().south.getU2(v, CubicGroup.Face.SOUTH),
                v.getUvCalculationStrategies().south.getV(v, CubicGroup.Face.SOUTH));
    }

    @Override
    public void createBottomVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz(),
                0,
                -1,
                0,
                v.getUvCalculationStrategies().bottom.getU(v, CubicGroup.Face.BOTTOM),
                v.getUvCalculationStrategies().bottom.getV(v, CubicGroup.Face.BOTTOM));
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz() + 1,
                0,
                -1,
                0,
                v.getUvCalculationStrategies().bottom.getU(v, CubicGroup.Face.BOTTOM),
                v.getUvCalculationStrategies().bottom.getV2(v, CubicGroup.Face.BOTTOM));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz() + 1,
                0,
                -1,
                0,
                v.getUvCalculationStrategies().bottom.getU2(v, CubicGroup.Face.BOTTOM),
                v.getUvCalculationStrategies().bottom.getV2(v, CubicGroup.Face.BOTTOM));
        vertices.addVertex8f(
                v.getWx() + 1,
                v.getWy(),
                v.getWz(),
                0,
                -1,
                0,
                v.getUvCalculationStrategies().bottom.getU2(v, CubicGroup.Face.BOTTOM),
                v.getUvCalculationStrategies().bottom.getV(v, CubicGroup.Face.BOTTOM));
    }

    @Override
    public void createWestVertices(Voxel v, VertexArray vertices) {
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz(),
                -1,
                0,
                0,
                v.getUvCalculationStrategies().west.getU(v, CubicGroup.Face.WEST),
                v.getUvCalculationStrategies().west.getV(v, CubicGroup.Face.WEST));
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + 1,
                v.getWz(),
                -1,
                0,
                0,
                v.getUvCalculationStrategies().west.getU(v, CubicGroup.Face.WEST),
                v.getUvCalculationStrategies().west.getV2(v, CubicGroup.Face.WEST));
        vertices.addVertex8f(
                v.getWx(),
                v.getWy() + 1,
                v.getWz() + 1,
                -1,
                0,
                0,
                v.getUvCalculationStrategies().west.getU2(v, CubicGroup.Face.WEST),
                v.getUvCalculationStrategies().west.getV2(v, CubicGroup.Face.WEST));
        vertices.addVertex8f(
                v.getWx(),
                v.getWy(),
                v.getWz() + 1,
                -1,
                0,
                0,
                v.getUvCalculationStrategies().west.getU2(v, CubicGroup.Face.WEST),
                v.getUvCalculationStrategies().west.getV(v, CubicGroup.Face.WEST));
    }
}
