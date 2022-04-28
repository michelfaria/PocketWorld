package vc.andro.poketest.voxel.rendering.uv;

import vc.andro.poketest.util.CubicGroup;

import static vc.andro.poketest.voxel.VoxelSpecs.VOXEL_TYPES;

public class DefaultUVCalculationStrategy implements UVCalculationStrategy {

    private static volatile DefaultUVCalculationStrategy sInstance = null;

    private DefaultUVCalculationStrategy() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + DefaultUVCalculationStrategy.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static DefaultUVCalculationStrategy getInstance() {
        if (sInstance == null) {
            synchronized (DefaultUVCalculationStrategy.class) {
                if (sInstance == null) {
                    sInstance = new DefaultUVCalculationStrategy();
                }
            }
        }
        return sInstance;
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
