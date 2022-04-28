package vc.andro.poketest.voxel.rendering.uv;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.util.CubicGroup;

import static vc.andro.poketest.PocketWorld.PPU;
import static vc.andro.poketest.voxel.VoxelSpecs.VOXEL_TYPES;

public class BigTextureUVCalculator implements UVCalculator {

    private static volatile BigTextureUVCalculator sInstance = null;

    private BigTextureUVCalculator() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + BigTextureUVCalculator.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static BigTextureUVCalculator getInstance() {
        if (sInstance == null) {
            synchronized (BigTextureUVCalculator.class) {
                if (sInstance == null) {
                    sInstance = new BigTextureUVCalculator();
                }
            }
        }
        return sInstance;
    }

    private float u;
    private float u2;
    private float v;
    private float v2;

    public synchronized void calculateUVs(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        TextureRegion txReg = VOXEL_TYPES[voxel].getTextureRegions().getFace(face);
        int regW = txReg.getRegionWidth();
        int regH = txReg.getRegionHeight();
        float regV = txReg.getV();
        float regV2 = txReg.getV2();
        float regU = txReg.getU();
        float regU2 = txReg.getU2();
        int tilesW = (int) (regW / PPU);
        int tilesH = (int) (regH / PPU);
        int partX = Math.floorMod(wx, tilesW);
        int partZ = Math.floorMod(wz, tilesH);
        float uDiff = regU2 - regU;
        float vDiff = regV2 - regV;
        float uPerTile = uDiff / tilesW;
        float vPerTile = vDiff / tilesH;

        u = regU + uPerTile * partX;
        u2 = regU + (uPerTile * partX) + uPerTile;
        v = regV + vPerTile * partZ;
        v2 = regV + (vPerTile * partZ) + vPerTile;
    }


    @Override
    public float getU(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        calculateUVs(face, voxel, wx, wy, wz);
        return u;
    }

    @Override
    public float getV(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        calculateUVs(face, voxel, wx, wy, wz);
        return v;
    }

    @Override
    public float getU2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        calculateUVs(face, voxel, wx, wy, wz);
        return u2;
    }

    @Override
    public float getV2(CubicGroup.Face face, byte voxel, int wx, int wy, int wz) {
        calculateUVs(face, voxel, wx, wy, wz);
        return v2;
    }
}
