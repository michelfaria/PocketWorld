package vc.andro.poketest.tools;

import vc.andro.poketest.util.FastNoise;

public class NoiseMinMax {
    public static void main(String[] args) {
        var noiseGen = new FastNoise();
        var amt = 1000;
        var min = Float.MAX_VALUE;
        var max = Float.MIN_VALUE;
        for (var i = 0; i < amt; i++) {
            for (var k = 0; k < amt; k++) {
                var result = noiseGen.GetNoise(i, k);
                if (result < min) {
                    min = result;
                }
                if (result > max) {
                    max = result;
                }
            }
        }
        System.out.println("min = " + min);
        System.out.println("max = " + max);
    }
}
