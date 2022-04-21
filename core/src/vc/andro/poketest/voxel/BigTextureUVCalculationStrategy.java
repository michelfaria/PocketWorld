package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import vc.andro.poketest.util.CubicGroup;

import static vc.andro.poketest.PocketWorld.PPU;

public class BigTextureUVCalculationStrategy implements UVCalculationStrategy, Pool.Poolable {

    public static final Pool<BigTextureUVCalculationStrategy> POOL = Pools.get(BigTextureUVCalculationStrategy.class);

    private Voxel voxel;
    private CubicGroup.Face whichFace;

    private float u;
    private float u2;
    private float v;
    private float v2;

    public BigTextureUVCalculationStrategy() {
    }

    public void init(Voxel voxel, CubicGroup.Face whichFace) {
        this.voxel = voxel;
        this.whichFace = whichFace;
        refresh();
    }

    @Override
    public void reset() {
        voxel = null;
        whichFace = null;
        u = 0;
        v = 0;
        u2 = 0;
        v2 = 0;
    }

    @Override
    public void refresh() {
        TextureRegion txReg = voxel.getTextureRegion(whichFace);
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
    public float getU(Voxel voxel, CubicGroup.Face face) {
        return u;
    }

    @Override
    public float getV(Voxel voxel, CubicGroup.Face face) {
        return v;
    }

    @Override
    public float getU2(Voxel voxel, CubicGroup.Face face) {
        return u2;
    }

    @Override
    public float getV2(Voxel voxel, CubicGroup.Face face) {
        return v2;
    }
}
