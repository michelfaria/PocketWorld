package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.world.Chunk;

public class TreeEntitySpawner implements EntitySpawner<ProspectorResult>
{
    @Override
    public void spawnEntity(ProspectorResult result, Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz) {
        TreeEntity tree = new TreeEntity();
        tree.setPosition(wx, y, wz);
        chunk.world.addEntity(tree);
    }
}
