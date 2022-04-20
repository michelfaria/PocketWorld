package vc.andro.poketest.voxel;

public class DefaultUVCalculationStrategy implements UVCalculationStrategy{

    private final BasicVoxel voxel;

    public DefaultUVCalculationStrategy(BasicVoxel voxel) {
        this.voxel = voxel;
    }

    @Override
    public float getU() {
        return voxel.getTextureRegion().getU();
    }

    @Override
    public float getV() {
        return voxel.getTextureRegion().getV();
    }

    @Override
    public float getU2() {
        return voxel.getTextureRegion().getU2();
    }

    @Override
    public float getV2() {
        return voxel.getTextureRegion().getV2();
    }
}
