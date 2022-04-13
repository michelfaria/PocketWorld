package vc.andro.poketest.world;

import java.util.Random;

public class WorldCreationParams {

    private static final int DEFAULT_WORLD_SEED = new Random().nextInt();
    private static final int DEFAULT_WORLD_WATER_LEVEL = 64;
    private static final int DEFAULT_WORLD_TERRACES = 0;
    private static final int DEFAULT_WORLD_BEACH_ALTITUDE = 68;
    private static final int DEFAULT_TREE_MAP_R_VALUE = 2;
    private static final boolean DEFAULT_ISLAND_MODE = false;
    private static final float DEFAULT_VALLEY_FACTOR = 1f;
    private static final float DEFAULT_ISLAND_MODE_SIZE = 200;
    private static final float DEFAULT_ALTITUDE_MAP_FREQUENCY = 1f;
    private static final int DEFAULT_ALTITUDE_MAP_OCTAVES = 4;
    private static final int DEFAULT_ALTITUDE_OFFSET = 20;

    public final int seed;
    public final int waterLevel;
    public final int terraces;
    public final int beachAltitude;
    public final int treeMapRValue;
    public final boolean islandMode;
    public final float valleyFactor;
    public final float islandModeSize;
    public final float altitudeMapFrequency;
    public final int altitudeMapOctaves;
    public final int altitudeOffset;

    public WorldCreationParams(int seed, int waterLevel, int terraces, int beachAltitude,
                               int treeMapRValue, boolean islandMode, float valleyFactor, float islandModeSize, float altitudeMapFrequency, int altitudeMapOctaves, int altitudeOffset) {
        this.seed = seed;
        this.waterLevel = waterLevel;
        this.terraces = terraces;
        this.beachAltitude = beachAltitude;
        this.treeMapRValue = treeMapRValue;
        this.islandMode = islandMode;
        this.valleyFactor = valleyFactor;
        this.islandModeSize = islandModeSize;
        this.altitudeMapFrequency = altitudeMapFrequency;
        this.altitudeMapOctaves = altitudeMapOctaves;
        this.altitudeOffset = altitudeOffset;
    }

    public WorldCreationParams() {
        this(DEFAULT_WORLD_SEED, DEFAULT_WORLD_WATER_LEVEL,
                DEFAULT_WORLD_TERRACES, DEFAULT_WORLD_BEACH_ALTITUDE, DEFAULT_TREE_MAP_R_VALUE,
                DEFAULT_ISLAND_MODE, DEFAULT_VALLEY_FACTOR, DEFAULT_ISLAND_MODE_SIZE,
                DEFAULT_ALTITUDE_MAP_FREQUENCY, DEFAULT_ALTITUDE_MAP_OCTAVES, DEFAULT_ALTITUDE_OFFSET);
    }
}
