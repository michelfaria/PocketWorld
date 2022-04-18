package vc.andro.poketest.world.map;

import vc.andro.poketest.world.NoiseGenerator;

public abstract class VegetationMapGenerator {

    private final NoiseGenerator noiseGenerator;

    public VegetationMapGenerator(NoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    public abstract int getRValue();

    public int getAtPosition(int worldX, int worldZ) {
        var r = getRValue();
        double max = 0;
        for (int xn = worldX - r; xn <= worldX + r; xn++) {
            for (int zn = worldZ - r; zn <= worldZ + r; zn++) {
                double e = noiseGenerator.getNoise(xn, zn);
                if (e > max) {
                    max = e;
                }
            }
        }
        if (noiseGenerator.getNoise(worldX, worldZ) == max) {
            return 1;
        }
        return 0;
    }
}
