package vc.andro.poketest.world;

public class TreeMapGenerator {

    private final NoiseGenerator noiseGenerator;
    private final WorldCreationParams creationParams;

    public TreeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams) {
        this.noiseGenerator = noiseGenerator;
        this.creationParams = creationParams;
    }

    public int getTreeAtPos(int worldX, int worldY) {
        final var r = creationParams.treeMapRValue;
        double max = 0;
        for (int xn = worldX - r; xn <= worldX + r; xn++) {
            for (int yn = worldY - r; yn <= worldY + r; yn++) {
                double e = noiseGenerator.getNoise(xn, yn);
                if (e > max) {
                    max = e;
                }
            }
        }
        if (noiseGenerator.getNoise(worldX, worldY) == max) {
            return 1;
        }
        return 0;
    }
}
