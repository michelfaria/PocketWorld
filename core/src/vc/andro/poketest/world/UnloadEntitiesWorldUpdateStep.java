package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import vc.andro.poketest.entity.Entity;

import static vc.andro.poketest.world.World.WxCx;
import static vc.andro.poketest.world.World.WzCz;

public class UnloadEntitiesWorldUpdateStep implements WorldUpdateStep {

    private static volatile UnloadEntitiesWorldUpdateStep sInstance = null;

    private UnloadEntitiesWorldUpdateStep() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + UnloadEntitiesWorldUpdateStep.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static UnloadEntitiesWorldUpdateStep getInstance() {
        if (sInstance == null) {
            synchronized (UnloadEntitiesWorldUpdateStep.class) {
                if (sInstance == null) {
                    sInstance = new UnloadEntitiesWorldUpdateStep();
                }
            }
        }
        return sInstance;
    }

    private final Array<Entity> entities = new Array<>(Entity.class);

    @Override
    public void update(World world, float delta) {
        entities.clear();
        world.getEntities().forEach(entity -> {
            if (world.isChunkOutsideOfRenderDistance_CP(WxCx(entity.getWx()), WzCz(entity.getWz()))) {
                entities.add(entity);
            }
        });
        for (Entity entity : entities) {
            world.removeEntity(entity);
        }
    }
}