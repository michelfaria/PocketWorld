package vc.andro.poketest.world;

public class AltitudeMapGenerator {

    private final NoiseGenerator noiseGenerator;
    private final WorldCreationParams creationParams;

    public AltitudeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams) {
        this.noiseGenerator = noiseGenerator;
        this.creationParams = creationParams;
    }

    public float altitudeAtPos(int worldX, int worldY) {
        final var islandMode = creationParams.islandMode;
        final var valleyFactor = creationParams.valleyFactor;
        final var terraces = creationParams.terraces;

        var elevation =
                // first octave
                noiseGenerator.getNoise(worldX, worldY)
                        // second octave
                        + 0.5f * noiseGenerator.getNoise(2f * worldX, 2f * worldY)
                        // third octave
                        + 0.25f * noiseGenerator.getNoise(4f * worldX, 4f * worldY);

        // keep e within range after adding multiple octaves
        elevation = elevation / (1f + 0.5f + 0.25f);
        // normalize elevation to 0.0 - 1.0 (from -1.0 - 1.0)
        elevation = (elevation + 1f) / 2f;

        if (islandMode) {
            var nx = 2f * (worldX - creationParams.islandModeSize / 2) / creationParams.islandModeSize;
            var ny = 2f * (worldY - creationParams.islandModeSize / 2) / creationParams.islandModeSize;

            var distance = 1f - (1f - (float) Math.pow(nx, 2f)) * (1f - (float) Math.pow(ny, 2f));
            distance += 0.2f;
            // apply distance function to elevation
            elevation = (elevation + (1f - distance)) / 2f;
        }

        // apply valley factor
        elevation = (float) Math.pow(elevation, valleyFactor);

        // make terraces
        elevation = (float) Math.round(elevation * terraces) / terraces;

        return elevation;
    }
}
