package vc.andro.poketest.voxel.rendering.uv;

import vc.andro.poketest.util.CubicGroup;

public final class NullUVCalculationStrategy implements UVCalculationStrategy {

    private static volatile NullUVCalculationStrategy sInstance = null;

    private NullUVCalculationStrategy() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + NullUVCalculationStrategy.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static NullUVCalculationStrategy getInstance() {
        if (sInstance == null) {
            synchronized (NullUVCalculationStrategy.class) {
                if (sInstance == null) {
                    sInstance = new NullUVCalculationStrategy();
                }
            }
        }
        return sInstance;
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
