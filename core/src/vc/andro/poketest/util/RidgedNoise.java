package vc.andro.poketest.util;

import vc.andro.poketest.world.generation.FloatNoiseGenerator;

public class RidgedNoise implements FloatNoiseGenerator {

    private final FloatNoiseGenerator noiseGenerator;
    // how many "layers of detail" to generate; at least 1, but note this slows down with many octaves
    private final int octaves;
    //  often about {@code 1f / 32f}, but generally adjusted for the use case
    private final int frequency;
    // when {@code octaves} is 2 or more, this affects the change between layers
    private final int lacunarity;

    public RidgedNoise(FloatNoiseGenerator noiseGenerator, int octaves, int frequency, int lacunarity) {
        this.noiseGenerator = noiseGenerator;
        this.octaves = octaves;
        this.frequency = frequency;
        this.lacunarity = lacunarity;
    }

    /**
     * Generates ridged-multi noise with the given amount of octaves and specified lacunarity (the amount of
     * frequency change between octaves); gain is not used.
     *
     * @param x x
     * @param y y
     * @return noise as a float between -1f and 1f
     */
    @Override
    public float getAtPosition(float x, float y) {
        x *= frequency;
        y *= frequency;

        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(noiseGenerator.getAtPosition(x, y));
            correction += (exp *= 0.5f);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }
}
