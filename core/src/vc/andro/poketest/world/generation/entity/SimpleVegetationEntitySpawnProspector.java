package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.generation.IntNoiseGenerator;

public class SimpleVegetationEntitySpawnProspector implements SpawnProspector<ProspectorResult> {

    private final IntNoiseGenerator noiseGenerator;

    public SimpleVegetationEntitySpawnProspector(IntNoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    @Override
    public ProspectorResult prospect(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz) {
        var result = new ProspectorResult();

        if (noiseGenerator.getAtPosition(wx, wz) == 0) {
            return result;
        }
        BasicVoxel surfaceTile = chunk.getSurfaceTile(lx, lz);
        if (surfaceTile == null) {
            return result;
        }

        int y = surfaceTile.y + 1;

        BasicVoxel tile = chunk.world.getTileAt_WP(wx, y, wz);
        if (tile != null) {
            return result;
        }

        BasicVoxel ty1 = chunk.world.getTileAt_WP(wx, y - 1, wz);

        if (ty1 == null || !ty1.type.equals(VoxelType.GRASS)) {
            return result;
        }

        result.shouldSpawn = true;
        result.spawnY = y;
        return result;

    }
}
