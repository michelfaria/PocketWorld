package vc.andro.poketest.world;

public class TreeMapGenerator {

    private final NoiseGenerator noiseGenerator;
    private final WorldCreationParams creationParams;

    public TreeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams) {
        this.noiseGenerator = noiseGenerator;
        this.creationParams = creationParams;
    }

    public int getTreeAtPos(int worldX, int worldZ) {
        final var r = creationParams.treeMapRValue;
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
