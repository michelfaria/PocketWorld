package vc.andro.poketest.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
}
