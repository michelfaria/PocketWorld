package vc.andro.poketest.world;

public class AltitudeMapGenerator {

    private final NoiseGenerator noiseGenerator;
    private final WorldCreationParams creationParams;

    public AltitudeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams) {
        this.noiseGenerator = noiseGenerator;
        this.creationParams = creationParams;
    }

    public float[][] generate() {
        final var width = creationParams.getWidth();
        final var height = creationParams.getHeight();
        final var islandMode = creationParams.isIslandMode();
        final var valleyFactor = creationParams.getValleyFactor();
        final var terraces = creationParams.getTerraces();

        float[][] elevations = new float[width][height];

        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var nx = 2f * (x - width / 2) / width;
                var ny = 2f * (y - height / 2) / height;

                var elevation =
                        // first octave
                        noiseGenerator.getNoise(x, y)
                                // second octave
                                + 0.5f * noiseGenerator.getNoise(2f * x, 2f * y)
                                // third octave
                                + 0.25f * noiseGenerator.getNoise(4f * x, 4f * y);
                // keep e within range after adding multiple octaves
                elevation = elevation / (1f + 0.5f + 0.25f);
                // normalize elevation to 0.0 - 1.0 (from -1.0 - 1.0)
                elevation = (elevation + 1f) / 2f;

                if (islandMode) {
                    var distance = 1f - (1f - (float) Math.pow(nx, 2f)) * (1f - (float) Math.pow(ny, 2f));
                    distance += 0.2f;
                    // apply distance function to elevation
                    elevation = (elevation + (1f - distance)) / 2f;
                }

                // apply valley factor
                elevation = (float) Math.pow(elevation, valleyFactor);

                // make terraces
                elevation = (float) Math.round(elevation * terraces) / terraces;

                // set elevation at (x, y)
                elevations[x][y] = elevation;
            }
        }

        return elevations;
    }
}
