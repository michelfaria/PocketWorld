package vc.andro.poketest.world;

import java.util.Random;

public class WorldCreationParams {

    private static final int DEFAULT_WORLD_SEED = new Random().nextInt();
    private static final float DEFAULT_WORLD_WATER_LEVEL = 0.4f;
    private static final int DEFAULT_WORLD_TERRACES = 48;
    private static final float DEFAULT_WORLD_BEACH_ALTITUDE = 0.45f;
    private static final int DEFAULT_TREE_MAP_R_VALUE = 4;
    private static final boolean DEFAULT_ISLAND_MODE = false;
    private static final float DEFAULT_VALLEY_FACTOR = 1.8f;
    private static final float DEFAULT_SLOPE_CHANCE = 0.05f;
    private static final float DEFAULT_ISLAND_MODE_SIZE = 1000;

    public final int seed;
    public final float waterLevel;
    public final int terraces;
    public final float beachAltitude;
    public final int treeMapRValue;
    public final boolean islandMode;
    public final float valleyFactor;
    public final float slopeChance;
    public final float islandModeSize;

    public WorldCreationParams(int seed,float waterLevel, int terraces, float beachAltitude,
                               int treeMapRValue, boolean islandMode, float valleyFactor, float slopeChance, float islandModeSize) {
        this.seed = seed;
        this.waterLevel = waterLevel;
        this.terraces = terraces;
        this.beachAltitude = beachAltitude;
        this.treeMapRValue = treeMapRValue;
        this.islandMode = islandMode;
        this.valleyFactor = valleyFactor;
        this.slopeChance = slopeChance;
        this.islandModeSize = islandModeSize;
    }

    public WorldCreationParams() {
        this(DEFAULT_WORLD_SEED, DEFAULT_WORLD_WATER_LEVEL,
                DEFAULT_WORLD_TERRACES, DEFAULT_WORLD_BEACH_ALTITUDE, DEFAULT_TREE_MAP_R_VALUE,
                DEFAULT_ISLAND_MODE, DEFAULT_VALLEY_FACTOR, DEFAULT_SLOPE_CHANCE, DEFAULT_ISLAND_MODE_SIZE);
    }
}
