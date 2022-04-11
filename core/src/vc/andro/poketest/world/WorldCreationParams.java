package vc.andro.poketest.world;

import java.util.Random;

public class WorldCreationParams {

    private static final int DEFAULT_WORLD_SEED = new Random().nextInt();
    private static final int DEFAULT_WORLD_WIDTH = 500;
    private static final int DEFAULT_WORLD_HEIGHT = 500;
    private static final float DEFAULT_WORLD_WATER_LEVEL = 0.4f;
    private static final int DEFAULT_WORLD_TERRACES = 48;
    private static final float DEFAULT_WORLD_BEACH_ALTITUDE = 0.45f;
    private static final int DEFAULT_TREE_MAP_R_VALUE = 4;
    private static final boolean DEFAULT_ISLAND_MODE = true;
    private static final float DEFAULT_VALLEY_FACTOR = 1.8f;
    private static final float DEFAULT_SLOPE_CHANCE = 0.05f;

    private final int seed;
    private final int width;
    private final int height;
    private final float waterLevel;
    private final int terraces;
    private final float beachAltitude;
    private final int treeMapRValue;
    private final boolean islandMode;
    private final float valleyFactor;
    private final float slopeChance;

    public WorldCreationParams(int seed, int width, int height, float waterLevel, int terraces, float beachAltitude,
                               int treeMapRValue, boolean islandMode, float valleyFactor, float slopeChance) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.waterLevel = waterLevel;
        this.terraces = terraces;
        this.beachAltitude = beachAltitude;
        this.treeMapRValue = treeMapRValue;
        this.islandMode = islandMode;
        this.valleyFactor = valleyFactor;
        this.slopeChance = slopeChance;
    }

    public WorldCreationParams() {
        this(DEFAULT_WORLD_SEED, DEFAULT_WORLD_WIDTH, DEFAULT_WORLD_HEIGHT, DEFAULT_WORLD_WATER_LEVEL,
                DEFAULT_WORLD_TERRACES, DEFAULT_WORLD_BEACH_ALTITUDE, DEFAULT_TREE_MAP_R_VALUE,
                DEFAULT_ISLAND_MODE, DEFAULT_VALLEY_FACTOR, DEFAULT_SLOPE_CHANCE);
    }

    public int getSeed() {
        return seed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public int getTerraces() {
        return terraces;
    }

    public float getBeachAltitude() {
        return beachAltitude;
    }

    public int getTreeMapRValue() {
        return treeMapRValue;
    }

    public boolean isIslandMode() {
        return islandMode;
    }

    public float getValleyFactor() {
        return valleyFactor;
    }

    public float getSlopeChance() {
        return slopeChance;
    }
}
