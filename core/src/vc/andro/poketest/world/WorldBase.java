package vc.andro.poketest.world;

public class WorldBase {

    private final WorldCreationParams creationParams;
    private final float[][] altitudeMap;
    private final int[][] treeMap;

    public WorldBase(WorldCreationParams creationParams, float[][] altitudeMap, int[][] treeMap) {
        this.creationParams = creationParams;
        this.altitudeMap = altitudeMap;
        this.treeMap = treeMap;
    }

    public int getSeed() {
        return creationParams.getSeed();
    }

    public int getWidth() {
        return creationParams.getWidth();
    }

    public int getHeight() {
        return creationParams.getHeight();
    }

    public float getSlopeChance() {
        return creationParams.getSlopeChance();
    }

    public float getWaterLevel() {
        return creationParams.getWaterLevel();
    }

    public float getBeachAltitude() {
        return creationParams.getBeachAltitude();
    }

    public float[][] getAltitudeMap() {
        return altitudeMap;
    }

    public int[][] getTreeMap() {
        return treeMap;
    }

    public int getDepth() {
        return creationParams.getDepth();
    }
}
