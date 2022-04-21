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
    public float getU(Voxel voxel, CubicGroup.Face face) {
        return 0;
    }

    @Override
    public float getV(Voxel voxel, CubicGroup.Face face) {
        return 0;
    }

    @Override
    public float getU2(Voxel voxel, CubicGroup.Face face) {
        return 0;
    }

    @Override
    public float getV2(Voxel voxel, CubicGroup.Face face) {
        return 0;
    }
}
