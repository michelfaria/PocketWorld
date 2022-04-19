package vc.andro.poketest.world.generation;

import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.generation.map.FlowerMapGenerator;

public class FlowerEntitySpawnProspector implements WorldGenEntitySpawner.SpawnProspector {

    private final FlowerMapGenerator flowerMapGenerator;
    private @Nullable Integer spawnY = null;

    public FlowerEntitySpawnProspector(FlowerMapGenerator flowerMapGenerator) {
        this.flowerMapGenerator = flowerMapGenerator;
    }

    @Override
    public boolean meetsCondition(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz) {
        spawnY = null;
        if (flowerMapGenerator.getAtPosition(wx, wz) == 0) {
            return false;
        }
        BasicVoxel surfaceTile = chunk.getSurfaceTile(lx, lz);
        if (surfaceTile == null) {
            return false;
        }
        int y = surfaceTile.y + 1;
        BasicVoxel tile = chunk.world.getTileAt_WP(wx, y, wz);
        if (tile != null) {
            return false;
        }
        BasicVoxel ty1 = chunk.world.getTileAt_WP(wx, y - 1, wz);

        if (ty1 != null && ty1.type.equals(VoxelType.GRASS)) {
            spawnY = y;
            return true;
        }
        return false;
    }

    @Override
    public int getSpawnY() {
        if (spawnY == null) {
            throw new NullPointerException("No Y set");
        }
        return spawnY;
    }
}
