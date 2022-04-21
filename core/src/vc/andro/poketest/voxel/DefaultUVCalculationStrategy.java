package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public class DefaultUVCalculationStrategy implements UVCalculationStrategy{

    private final BasicVoxel voxel;
    private final CubicGroup.Face face;

    public DefaultUVCalculationStrategy(BasicVoxel voxel, CubicGroup.Face face) {
        this.voxel = voxel;
        this.face = face;
    }

    @Override
    public float getU() {
        return voxel.getTextureRegion(face).getU();
    }

    @Override
    public float getV() {
        return voxel.getTextureRegion(face).getV();
    }

    @Override
    public float getU2() {
        return voxel.getTextureRegion(face).getU2();
    }

    @Override
    public float getV2() {
        return voxel.getTextureRegion(face).getV2();
    }
}
