package vc.andro.poketest.voxel;

import vc.andro.poketest.util.CubicGroup;

public interface UVCalculationStrategy {

    float getU(Voxel voxel, CubicGroup.Face face);

    float getV(Voxel voxel, CubicGroup.Face face);

    float getU2(Voxel voxel, CubicGroup.Face face);

    float getV2(Voxel voxel, CubicGroup.Face face);
}
