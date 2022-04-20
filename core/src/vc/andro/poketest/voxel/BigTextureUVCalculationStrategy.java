package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static vc.andro.poketest.PocketWorld.PPU;

public class BigTextureUVCalculationStrategy implements UVCalculationStrategy {

    private final BasicVoxel voxel;

    private float u;
    private float u2;
    private float v;
    private float v2;

    public BigTextureUVCalculationStrategy(BasicVoxel voxel) {
        this.voxel = voxel;
        refresh();
    }

    @Override
    public void refresh() {
        TextureRegion txReg = voxel.getTextureRegion();
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
    public float getU() {
        return u;
    }

    @Override
    public float getV() {
        return v;
    }

    @Override
    public float getU2() {
        return u2;
    }

    @Override
    public float getV2() {
        return v2;
    }
}
