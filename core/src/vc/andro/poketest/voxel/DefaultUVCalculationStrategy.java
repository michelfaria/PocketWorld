package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public class DefaultUVCalculationStrategy implements UVCalculationStrategy {

    private static DefaultUVCalculationStrategy INSTANCE;

    public static synchronized DefaultUVCalculationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultUVCalculationStrategy();
        }
        return INSTANCE;
    }

    @Override
    public float getU(Voxel voxel, CubicGroup.Face face) {
        return voxel.getTextureRegion(face).getU();
    }

    @Override
    public float getV(Voxel voxel, CubicGroup.Face face) {
        return voxel.getTextureRegion(face).getV();
    }

    @Override
    public float getU2(Voxel voxel, CubicGroup.Face face) {
        return voxel.getTextureRegion(face).getU2();
    }

    @Override
    public float getV2(Voxel voxel, CubicGroup.Face face) {
        return voxel.getTextureRegion(face).getV2();
    }
}
