package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

import static vc.andro.poketest.voxel.VoxelSpecs.VOXEL_TYPES;

public class DefaultUVCalculationStrategy implements UVCalculationStrategy {

    private static DefaultUVCalculationStrategy INSTANCE;

    public static synchronized DefaultUVCalculationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultUVCalculationStrategy();
        }
        return INSTANCE;
    }

    @Override
    public float getU(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return VOXEL_TYPES[voxel].textureRegions.getFace(face).getU();
    }

    @Override
    public float getV(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return VOXEL_TYPES[voxel].textureRegions.getFace(face).getV();
    }

    @Override
    public float getU2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return VOXEL_TYPES[voxel].textureRegions.getFace(face).getU2();
    }

    @Override
    public float getV2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        return VOXEL_TYPES[voxel].textureRegions.getFace(face).getV2();
    }
}
