package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.util.CubicGroup;

import static vc.andro.poketest.PocketWorld.PPU;

public class BigTextureUVCalculationStrategy implements UVCalculationStrategy {

    private static BigTextureUVCalculationStrategy INSTANCE;

    public static BigTextureUVCalculationStrategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BigTextureUVCalculationStrategy();
        }
        return INSTANCE;
    }

    private BigTextureUVCalculationStrategy() {
    }

    private float u;
    private float u2;
    private float v;
    private float v2;

    public synchronized void calculateUVs(Voxel voxel, CubicGroup.Face face) {
        TextureRegion txReg = voxel.getType().textureRegions.getFace(face);
        int regW = txReg.getRegionWidth();
        int regH = txReg.getRegionHeight();
        float regV = txReg.getV();
        float regV2 = txReg.getV2();
        float regU = txReg.getU();
        float regU2 = txReg.getU2();
        int voxelWz = voxel.getWz();
        int voxelWx = voxel.getWx();
        int tilesW = (int) (regW / PPU);
        int tilesH = (int) (regH / PPU);
        int partX = Math.floorMod(voxelWx, tilesW);
        int partZ = Math.floorMod(voxelWz, tilesH);
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
    public synchronized float getU(Voxel voxel, CubicGroup.Face face) {
        calculateUVs(voxel, face);
        return u;
    }

    @Override
    public synchronized float getV(Voxel voxel, CubicGroup.Face face) {
        calculateUVs(voxel, face);
        return v;
    }

    @Override
    public synchronized float getU2(Voxel voxel, CubicGroup.Face face) {
        calculateUVs(voxel, face);
        return u2;
    }

    @Override
    public synchronized float getV2(Voxel voxel, CubicGroup.Face face) {
        calculateUVs(voxel, face);
        return v2;
    }
}
