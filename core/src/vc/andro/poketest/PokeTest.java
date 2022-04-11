package vc.andro.poketest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import vc.andro.poketest.world.WorldCreationParams;

public class PokeTest extends Game {

    public static final int TILE_SIZE = 16;

    public static AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load(Assets.textureAtlas);
        assetManager.load(Assets.hackFont8pt);
        assetManager.finishLoading();
        setScreen(new PlayScreen(new WorldCreationParams()));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
