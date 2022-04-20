package vc.andro.poketest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import vc.andro.poketest.world.WorldCreationParams;

public class PocketWorld extends Game {

    public static final float PPU = 16; // Pixels per unit (Tile size, basically)
    public static final int TICKS_PER_SECOND = 16;

    public static AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load(Assets.tileAtlas);
        assetManager.load(Assets.entityAtlas);
        assetManager.load(Assets.hackFont8pt);
        assetManager.finishLoading();
        setScreen(new PlayScreen(new WorldCreationParams()));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
