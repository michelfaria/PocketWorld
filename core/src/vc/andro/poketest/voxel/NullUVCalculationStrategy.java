package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public final class NullUVCalculationStrategy implements UVCalculationStrategy {

    private static NullUVCalculationStrategy INSTANCE;

    private NullUVCalculationStrategy() {
    }

    public static synchronized NullUVCalculationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NullUVCalculationStrategy();
        }
        return INSTANCE;
    }

    @Override
    public float getU(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return 0;
    }

    @Override
    public float getV(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return 0;
    }

    @Override
    public float getU2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return 0;
    }

    @Override
    public float getV2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return 0;
    }
}
