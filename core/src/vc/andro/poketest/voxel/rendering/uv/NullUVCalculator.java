package vc.andro.poketest.voxel.rendering.uv;

import vc.andro.poketest.util.CubicGroup;

public final class NullUVCalculator implements UVCalculator {

    private static volatile NullUVCalculator sInstance = null;

    private NullUVCalculator() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + NullUVCalculator.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static NullUVCalculator getInstance() {
        if (sInstance == null) {
            synchronized (NullUVCalculator.class) {
                if (sInstance == null) {
                    sInstance = new NullUVCalculator();
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
