package vc.andro.poketest.world.generation;

import vc.andro.poketest.entity.TreeEntity;
import vc.andro.poketest.world.Chunk;

public class TreeEntitySpawner implements WorldGenEntitySpawner.EntitySpawner
{
    @Override
    public void spawnEntity(Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz) {
        TreeEntity tree = new TreeEntity();
        tree.setPosition(wx, y, wz);
        chunk.world.addEntity(tree);
    }
}
