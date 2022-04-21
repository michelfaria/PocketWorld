package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public interface UVCalculationStrategy {

    /**
     * This must be called after a voxel's texture region or position is updated.
     */
    default void refresh() {
    }

    float getU(Voxel voxel, CubicGroup.Face face);

    float getV(Voxel voxel, CubicGroup.Face face);

    float getU2(Voxel voxel, CubicGroup.Face face);

    float getV2(Voxel voxel, CubicGroup.Face face);
}
