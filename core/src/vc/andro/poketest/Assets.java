package vc.andro.poketest;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class Assets {

    public static final AssetDescriptor<TextureAtlas> textureAtlas = new AssetDescriptor<>("poketest.atlas", TextureAtlas.class);

    private Assets() {
    }
}
