package vc.andro.poketest.world.generation.map;

import vc.andro.poketest.world.generation.NoiseGenerator;

public abstract class VegetationMapGenerator {

    private final NoiseGenerator noiseGenerator;

    public VegetationMapGenerator(NoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    public abstract int getRValue();

    public int getAtPosition(int wx, int wz) {
        var r = getRValue();
        double max = 0;
        for (int ix = wx - r; ix <= wx + r; ix++) {
            for (int iz = wz - r; iz <= wz + r; iz++) {
                double e = noiseGenerator.getNoise(ix, iz);
                if (e > max) {
                    max = e;
                }
            }
        }
        if (noiseGenerator.getNoise(wx, wz) == max) {
            return 1;
        }
        return 0;
    }
}
