package vc.andro.poketest.voxel.rendering.uv;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.NotNull;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.voxel.Voxel;

public class DefaultUVCalculator implements UVCalculator {

    private static volatile DefaultUVCalculator sInstance = null;

    private DefaultUVCalculator() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + DefaultUVCalculator.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static DefaultUVCalculator getInstance() {
        if (sInstance == null) {
            synchronized (DefaultUVCalculator.class) {
                if (sInstance == null) {
                    sInstance = new DefaultUVCalculator();
                }
            }
        }
        return sInstance;
    }

    @Override
    public float getU(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz) {
        return getTextureRegionsForVoxel(voxel).getFace(face).getU();
    }

    @Override
    public float getV(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz) {
        return getTextureRegionsForVoxel(voxel).getFace(face).getV();
    }

    @Override
    public float getU2(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz) {
        return getTextureRegionsForVoxel(voxel).getFace(face).getU2();
    }

    @Override
    public float getV2(CubicGroup.Face face, Voxel voxel, int wx, int wy, int wz) {
        return getTextureRegionsForVoxel(voxel).getFace(face).getV2();
    }

    @NotNull
    private CubicGroup<TextureRegion> getTextureRegionsForVoxel(Voxel voxel) {
        CubicGroup<TextureRegion> textureRegions = voxel.getTextureRegions();
        assert textureRegions != null : "Missing texture regions";
        return textureRegions;
    }
}
