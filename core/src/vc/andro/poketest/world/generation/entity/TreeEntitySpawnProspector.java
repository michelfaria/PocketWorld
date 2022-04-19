package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.tile.VoxelType;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.generation.map.TreeMapGenerator;

public class TreeEntitySpawnProspector implements SpawnProspector<ProspectorResult> {

    private final TreeMapGenerator treeMapGenerator;

    public TreeEntitySpawnProspector(TreeMapGenerator treeMapGenerator) {
        this.treeMapGenerator = treeMapGenerator;
    }

    @Override
    public ProspectorResult prospect(Chunk chunk, int wx, int wz, int cx, int cz, int lx, int lz) {
        var result = new ProspectorResult();

        if (treeMapGenerator.getAtPosition(wx, wz) == 0) {
            return result;
        }

        BasicVoxel surfaceTile = chunk.getSurfaceTile(lx, lz);
        if (surfaceTile == null) {
            return result;
        }

        int y = surfaceTile.y + 1;

        for (int ix = 0; ix < TreeEntity.COLLISION_WIDTH; ix++) {
            for (int iz = 0; iz < TreeEntity.COLLISION_HEIGHT; iz++) {

                BasicVoxel tileUnder = chunk.world.getTileAt_WP(wx + ix, y - 1, wz + iz);
                BasicVoxel tile = chunk.world.getTileAt_WP(wx + ix, y, wz + iz);

                if (tileUnder == null || !tileUnder.type.equals(VoxelType.GRASS) || tile != null) {
                    return result;
                }
            }
        }

        result.shouldSpawn = true;
        result.spawnY = y;
        return result;
    }
}
