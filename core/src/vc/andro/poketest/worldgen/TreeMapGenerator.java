package vc.andro.poketest.worldgen;

public class TreeMapGenerator {

    private final NoiseGenerator noiseGenerator;
    private final WorldCreationParams creationParams;

    private final float[][] altitudeMap; // TODO: Use this to influence tree frequency

    public TreeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams, float[][] altitudeMap) {
        this.noiseGenerator = noiseGenerator;
        this.altitudeMap = altitudeMap;
        this.creationParams = creationParams;
    }

    public int[][] generate() {
        final var width = creationParams.getWidth();
        final var height = creationParams.getHeight();
        final var r = creationParams.getTreeMapRValue();

        int[][] treeMap = new int[width][height];

        for (int xc = 0; xc < width; xc++) {
            for (int yc = 0; yc < height; yc++) {
                double max = 0;
                for (int xn = xc - r; xn <= xc + r; xn++) {
                    for (int yn = yc - r; yn <= yc + r; yn++) {
                        if (0 <= yn && yn < height && 0 <= xn && xn < width) {
                            double e = noiseGenerator.getNoise(xn, yn);
                            if (e > max) {
                                max = e;
                            }
                        }
                    }
                }
                if (noiseGenerator.getNoise(xc, yc) == max) {
                    treeMap[xc][yc] = 1;
                }
            }
        }
        return treeMap;
    }
}
