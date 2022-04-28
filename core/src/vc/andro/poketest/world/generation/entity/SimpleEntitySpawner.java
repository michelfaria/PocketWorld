package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.world.chunk.Chunk;

public class SimpleEntitySpawner<T extends Entity> implements Spawner<ProspectorResult> {

    private final Class<T> entityClass;

    public SimpleEntitySpawner(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void spawn(ProspectorResult prospectorResult, Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            entity.setPosition(wx, y, wz);
            chunk.getWorld().addEntity(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
