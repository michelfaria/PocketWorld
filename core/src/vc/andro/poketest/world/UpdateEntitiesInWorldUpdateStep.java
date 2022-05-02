package vc.andro.poketest.world;

import vc.andro.poketest.entity.Entity;

public class UpdateEntitiesInWorldUpdateStep implements WorldUpdateStep {
    @Override
    public void update(World world, float delta) {
        for (Entity entity : world.getEntities()) {
            entity.update(delta);
        }
    }
}
