package vc.andro.poketest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.world.WorldCreationParams;

import java.util.Objects;

public class PocketWorld extends Game {

    public static final float PPU              = 16; // Pixels per unit (Tile size, basically)
    public static final int   TICKS_PER_SECOND = 16;

    private @Nullable static AssetManager assetManager;
    private @Nullable        PlayScreen   playScreen;

    @NotNull
    public static AssetManager getAssetManager() {
        return Objects.requireNonNull(assetManager, "Asset manager is not available");
    }

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load(Assets.tileAtlas);
        assetManager.load(Assets.entityAtlas);
        assetManager.load(Assets.hackFont8pt);
        assetManager.finishLoading();

        playScreen = new PlayScreen(new WorldCreationParams());
        setScreen(playScreen);
    }

    @Override
    public void dispose() {
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (playScreen != null) {
            playScreen.dispose();
        }
    }
}
