package vc.andro.poketest.world.generation;

import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.generation.map.TreeMapGenerator;

public class TreeEntitySpawnProspector implements WorldGenEntitySpawner.SpawnProspector {

    private final TreeMapGenerator treeMapGenerator;
    private @Nullable Integer spawnY = null;

    public TreeEntitySpawnProspector(TreeMapGenerator treeMapGenerator) {
        this.treeMapGenerator = treeMapGenerator;
    }

    @Override
    public synchronized boolean meetsCondition(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz) {
        spawnY = null;
        BasicVoxel surfaceTile = chunk.getSurfaceTile(lx, lz);
        if (surfaceTile == null) {
            return false;
        }
        int y = surfaceTile.y + 1;
        for (int ix = 0; ix < TreeEntity.COLLISION_WIDTH; ix++) {
            for (int iz = 0; iz < TreeEntity.COLLISION_HEIGHT; iz++) {
                BasicVoxel tileUnder = chunk.world.getTileAt_WP(wx + ix, y - 1, wz + iz);
                BasicVoxel tile = chunk.world.getTileAt_WP(wx + ix, y, wz + iz);
                if (tileUnder == null || !tileUnder.type.equals(VoxelType.GRASS) || tile != null) {
                    return false;
                }
            }
        }
        spawnY = y;
        return treeMapGenerator.getAtPosition(wx, wz) > 0;
    }

    @Override
    public synchronized int getSpawnY() {
        if (spawnY == null) {
            throw new NullPointerException("No Y set");
        }
        return spawnY;
    }
}
