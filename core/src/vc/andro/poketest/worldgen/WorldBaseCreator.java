package vc.andro.poketest.worldgen;

import vc.andro.poketest.util.BlueNoise;
import vc.andro.poketest.util.FastNoise;

public class WorldBaseCreator {

    private final FastNoise perlinNoiseGenerator;
    private final BlueNoise blueNoiseGenerator;
    private final WorldCreationParams creationParams;

    public WorldBaseCreator(WorldCreationParams creationParams) {
        this.creationParams = creationParams;

        perlinNoiseGenerator = new FastNoise(creationParams.getSeed());
        perlinNoiseGenerator.SetNoiseType(FastNoise.NoiseType.Perlin);

        blueNoiseGenerator = new BlueNoise();
    }

    public WorldBase createBase() {
        float[][] altitudeMap = new AltitudeMapGenerator(perlinNoiseGenerator, creationParams).generate();
        int[][] treeMap = new TreeMapGenerator(blueNoiseGenerator, creationParams, altitudeMap).generate();
        return new WorldBase(creationParams, altitudeMap, treeMap);
    }
}
