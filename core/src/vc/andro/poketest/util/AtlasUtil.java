package vc.andro.poketest.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class AtlasUtil {
    private AtlasUtil() {
    }

    public static TextureRegion findRegion(TextureAtlas atlas, String regionName) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(regionName);
        if (region == null) {
            throw new NullPointerException("Region not found: " + regionName);
        }
        return region;
    }

    public static TextureRegion findRegion(TextureAtlas atlas, String regionName, int index) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(regionName, index);
        if (region == null) {
            throw new NullPointerException("Region not found: " + regionName);
        }
        return region;
    }



    public static Array<TextureAtlas.AtlasRegion> findRegions(TextureAtlas atlas, String regionName) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(regionName);
        if (regions == null || regions.isEmpty()) {
            throw new IllegalStateException("Region not found: " + regionName);
        }
        return regions;
    }
}
