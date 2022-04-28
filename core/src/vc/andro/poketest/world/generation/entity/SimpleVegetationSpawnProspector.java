package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.voxel.VoxelSpecs;
import vc.andro.poketest.world.chunk.Chunk;
import vc.andro.poketest.world.generation.IntNoiseGenerator;

import static vc.andro.poketest.world.chunk.Chunk.CHUNK_DEPTH;

public class SimpleVegetationSpawnProspector implements SpawnProspector<ProspectorResult> {

    private final IntNoiseGenerator noiseGenerator;

    private final int entityCollisionWidth;
    private final int entityCollisionHeight;

    public SimpleVegetationSpawnProspector(IntNoiseGenerator noiseGenerator) {
        this(noiseGenerator, 1, 1);
    }

    public SimpleVegetationSpawnProspector(IntNoiseGenerator noiseGenerator, int entityCollisionWidth, int entityCollisionHeight) {
        this.noiseGenerator = noiseGenerator;
        this.entityCollisionWidth = entityCollisionWidth;
        this.entityCollisionHeight = entityCollisionHeight;
    }

    @Override
    public ProspectorResult prospect(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz) {
        var result = new ProspectorResult();

        if (noiseGenerator.getAtPosition(wx, wz) == 0) {
            return result;
        }
        int surfaceVoxelWy = chunk.getSurfaceVoxelWy_LP(lx, lz);
        if (surfaceVoxelWy == -1) {
            return result;
        }

        int y = surfaceVoxelWy + 1;

        if (y + 1 > CHUNK_DEPTH) {
            return result;
        }

        for (int ix = 0; ix < entityCollisionWidth; ix++) {
            for (int iz = 0; iz < entityCollisionHeight; iz++) {
                byte tile = chunk.getWorld().getVoxelAt_WP(wx + ix, y, wz + iz);
                if (tile != 0) {
                    return result;
                }

                byte ty1 = chunk.getWorld().getVoxelAt_WP(wx + ix, y - 1, wz + iz);

                if (ty1 == 0 || VoxelSpecs.VOXEL_TYPES[ty1] != VoxelSpecs.GRASS) {
                    return result;
                }
            }
        }

        result.shouldSpawn = true;
        result.spawnY = y;
        return result;

    }
}
