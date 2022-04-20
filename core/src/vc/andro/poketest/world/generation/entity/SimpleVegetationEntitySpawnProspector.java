package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.generation.IntNoiseGenerator;

import static vc.andro.poketest.world.Chunk.CHUNK_DEPTH;

public class SimpleVegetationEntitySpawnProspector implements SpawnProspector<ProspectorResult> {

    private final IntNoiseGenerator noiseGenerator;

    private final int entityCollisionWidth;
    private final int entityCollisionHeight;

    public SimpleVegetationEntitySpawnProspector(IntNoiseGenerator noiseGenerator) {
        this(noiseGenerator, 1, 1);
    }

    public SimpleVegetationEntitySpawnProspector(IntNoiseGenerator noiseGenerator, int entityCollisionWidth, int entityCollisionHeight) {
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
        BasicVoxel surfaceTile = chunk.getSurfaceTile_LP(lx, lz);
        if (surfaceTile == null) {
            return result;
        }

        int y = surfaceTile.wy + 1;

        if (y + 1 > CHUNK_DEPTH) {
            return result;
        }

        for (int ix = 0; ix < entityCollisionWidth; ix++) {
            for (int iz = 0; iz < entityCollisionHeight; iz++) {
                BasicVoxel tile = chunk.world.getTileAt_WP(wx + ix, y, wz + iz);
                if (tile != null) {
                    return result;
                }

                BasicVoxel ty1 = chunk.world.getTileAt_WP(wx + ix, y - 1, wz + iz);

                if (ty1 == null || !ty1.type.equals(VoxelType.GRASS)) {
                    return result;
                }
            }
        }

        result.shouldSpawn = true;
        result.spawnY = y;
        return result;

    }
}
