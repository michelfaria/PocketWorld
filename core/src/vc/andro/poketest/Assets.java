package vc.andro.poketest;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class Assets {

    public static final AssetDescriptor<TextureAtlas> textureAtlas = new AssetDescriptor<>("poketest.atlas", TextureAtlas.class);
    public static final AssetDescriptor<BitmapFont> hackFont8pt = new AssetDescriptor<>("font-hack-8pt.fnt", BitmapFont.class);

    private Assets() {
    }
}
