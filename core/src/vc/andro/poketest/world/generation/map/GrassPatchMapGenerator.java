package vc.andro.poketest.world.generation.map;

import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.generation.FloatNoiseGenerator;
import vc.andro.poketest.world.generation.IntNoiseGenerator;

public class GrassPatchMapGenerator implements IntNoiseGenerator {

    private final FloatNoiseGenerator noiseGenerator;

    public GrassPatchMapGenerator(FloatNoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    @Override
    public int getAtPosition(int wx, int wz) {
        float value = noiseGenerator.getAtPosition(wx * 8, wz * 8);
        value = (value + 1.0f) / 2.0f;
        if (value > 0.5f) {
            return 1;
        }
        return 0;
    }
}

