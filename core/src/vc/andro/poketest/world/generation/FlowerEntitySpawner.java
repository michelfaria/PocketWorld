package vc.andro.poketest.world.generation;

import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.world.Chunk;

public class FlowerEntitySpawner implements WorldGenEntitySpawner.EntitySpawner {
    @Override
    public void spawnEntity(Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz) {
        var flower = new FlowerEntity();
        flower.setPosition(wx, y, wz);
        chunk.world.addEntity(flower);
    }
}
