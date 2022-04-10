package vc.andro.poketest.worldgen;

public class WorldCreationParams {
    private final int seed;
    private final int width;
    private final int height;
    private final int depth;
    private final float waterLevel;
    private final int terraces;
    private final float beachAltitude;
    private final int treeMapRValue;
    private final boolean islandMode;
    private final float valleyFactor;
    private final float slopeChance;

    public WorldCreationParams(int seed, int width, int height, int depth, float waterLevel, int terraces, float beachAltitude,
                               int treeMapRValue, boolean islandMode, float valleyFactor, float slopeChance) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.waterLevel = waterLevel;
        this.terraces = terraces;
        this.beachAltitude = beachAltitude;
        this.treeMapRValue = treeMapRValue;
        this.islandMode = islandMode;
        this.valleyFactor = valleyFactor;
        this.slopeChance = slopeChance;
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

    public int getDepth() {
        return depth;
    }
}
