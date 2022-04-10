package vc.andro.poketest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import vc.andro.poketest.worldgen.WorldCreationParams;

import java.util.Random;

public class PokeTest extends Game {

    public static final int TILE_SIZE = 16;
    public static final int VIEWPORT_HEIGHT = 600;
    public static final int VIEWPORT_WIDTH = 800;

    private static final int WORLD_SEED = new Random().nextInt();
    private static final int WORLD_WIDTH = 500;
    private static final int WORLD_HEIGHT = 500;
    private static final int WORLD_DEPTH = 100;
    private static final float WORLD_WATER_LEVEL = 0.4f;
    private static final int WORLD_TERRACES = 48;
    private static final float WORLD_BEACH_ALTITUDE = 0.45f;
    private static final int TREE_MAP_R_VALUE = 4;
    private static final boolean ISLAND_MODE = true;
    private static final float VALLEY_FACTOR = 1.0f;
    private static final float SLOPE_CHANCE = 0.05f;

    private static final WorldCreationParams WORLD_CREATION_PARAMS = new WorldCreationParams(
            WORLD_SEED, WORLD_WIDTH, WORLD_HEIGHT, WORLD_DEPTH, WORLD_WATER_LEVEL, WORLD_TERRACES, WORLD_BEACH_ALTITUDE,
            TREE_MAP_R_VALUE, ISLAND_MODE, VALLEY_FACTOR, SLOPE_CHANCE);

    public static AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load(Assets.textureAtlas);
        assetManager.load(Assets.hackFont8pt);
        assetManager.finishLoading();
        setScreen(new PlayScreen(WORLD_CREATION_PARAMS));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
