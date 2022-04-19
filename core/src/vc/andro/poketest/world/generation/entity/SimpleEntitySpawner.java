package vc.andro.poketest.world.generation.entity;

import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.world.Chunk;

public class SimpleEntitySpawner<T extends Entity> implements EntitySpawner<ProspectorResult> {

    private final Class<T> entityClass;

    public SimpleEntitySpawner(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void spawnEntity(ProspectorResult prospectorResult, Chunk chunk, int wx, int y, int wz, int cx, int cz, int lx, int lz) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            entity.setPosition(wx, y, wz);
            chunk.world.addEntity(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
