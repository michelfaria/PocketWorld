package vc.andro.poketest.world;

import com.badlogic.gdx.math.MathUtils;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;

public class AltitudeMapGenerator {

    private final NoiseGenerator noiseGenerator;
    private final WorldCreationParams creationParams;

    public AltitudeMapGenerator(NoiseGenerator noiseGenerator, WorldCreationParams creationParams) {
        this.noiseGenerator = noiseGenerator;
        this.creationParams = creationParams;
    }

    public int altitudeAtPos(int worldX, int worldZ) {
        float elevation = 0.0f;
        {
            float amplitudeSum = 0.0f;
            for (int octave = 1; octave <= creationParams.altitudeMapOctaves; octave++) {
                float amplitude = 1.0f / (float) octave;
                elevation += amplitude * noiseGenerator.getNoise(
                        worldX * creationParams.altitudeMapFrequency * octave,
                        worldZ * creationParams.altitudeMapFrequency * octave
                );
                amplitudeSum += amplitude;
            }
            // keep e within [-1.0..1.0] range after adding multiple octaves
            elevation /= amplitudeSum;
        }

        // normalize elevation to [0.0..1.0] (from [-1.0 - 1.0])
        elevation = (elevation + 1f) / 2f;

        if (creationParams.islandMode) {
            var nx = 2f * (worldX - creationParams.islandModeSize / 2) / creationParams.islandModeSize;
            var ny = 2f * (worldZ - creationParams.islandModeSize / 2) / creationParams.islandModeSize;

            float distance = 1f - (1f - (float) Math.pow(nx, 2f)) * (1f - (float) Math.pow(ny, 2f));
            distance += 0.2f;

            // apply distance function to elevation
            elevation = (elevation + (1f - distance)) / 2f;
        }

        // apply valley factor
        elevation = (float) Math.pow(elevation, creationParams.valleyFactor);
        if (creationParams.terraces > 0) {
            // make terraces
            elevation = (float) Math.round(elevation * creationParams.terraces) / creationParams.terraces;
        }
        // adapt elevation to chunk depth
        elevation *= CHUNK_DEPTH;

        int elevationBlocks = Math.round(elevation);
        elevationBlocks += creationParams.altitudeOffset;
        elevationBlocks = MathUtils.clamp(elevationBlocks, 0, CHUNK_DEPTH - 1);
        return elevationBlocks;
    }
}
