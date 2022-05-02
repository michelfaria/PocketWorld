package vc.andro.poketest.voxel.rendering.uv;

import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.voxel.Voxel;

public interface UVCalculator {

    float getU(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz);

    float getV(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz);

    float getU2(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz);

    float getV2(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz);
}
