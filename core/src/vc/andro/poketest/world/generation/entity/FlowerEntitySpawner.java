package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.entity.FlowerEntity;
import vc.andro.poketest.world.Chunk;

public class FlowerEntitySpawner implements EntitySpawner<ProspectorResult> {

    @Override
    public void spawnEntity(ProspectorResult prospectorResult, Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz) {
        var flower = new FlowerEntity();
        flower.setPosition(wx, y, wz);
        chunk.world.addEntity(flower);
    }
}
