package vc.andro.poketest.world.generation.map;

import vc.andro.poketest.world.generation.FloatNoiseGenerator;
import vc.andro.poketest.world.generation.IntNoiseGenerator;

public abstract class VegetationMapGenerator implements IntNoiseGenerator {

    private final FloatNoiseGenerator noiseGenerator;

    public VegetationMapGenerator(FloatNoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    public abstract int getRValue();

    @Override
    public int getAtPosition(int wx, int wz) {
        int r = getRValue();
        double max = 0;
        for (int ix = wx - r; ix <= wx + r; ix++) {
            for (int iz = wz - r; iz <= wz + r; iz++) {
                double e = noiseGenerator.getAtPosition(ix, iz);
                if (e > max) {
                    max = e;
                }
            }
        }
        if (noiseGenerator.getAtPosition(wx, wz) == max) {
            return 1;
        }
        return 0;
    }
}
