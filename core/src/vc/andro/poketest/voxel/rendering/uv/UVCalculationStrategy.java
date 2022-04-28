package vc.andro.poketest.voxel.rendering.uv;

import vc.andro.poketest.util.CubicGroup;

public interface UVCalculationStrategy {

    float getU(CubicGroup.Face face, byte voxel, int wx, int wy, int wz);

    float getV(CubicGroup.Face face, byte voxel, int wx, int wy, int wz);

    float getU2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz);

    float getV2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz);
}
