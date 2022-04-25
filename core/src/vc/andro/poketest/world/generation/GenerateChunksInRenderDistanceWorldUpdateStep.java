package vc.andro.poketest.world.generation;

import vc.andro.poketest.registry.RenderSettingsRegistry;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldUpdateStep;

import static vc.andro.poketest.world.World.WxCx;
import static vc.andro.poketest.world.World.WzCz;

public class GenerateChunksInRenderDistanceWorldUpdateStep implements WorldUpdateStep {
    private static volatile GenerateChunksInRenderDistanceWorldUpdateStep sInstance = null;

    private GenerateChunksInRenderDistanceWorldUpdateStep() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + GenerateChunksInRenderDistanceWorldUpdateStep.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static GenerateChunksInRenderDistanceWorldUpdateStep getInstance() {
        if (sInstance == null) {
            synchronized (GenerateChunksInRenderDistanceWorldUpdateStep.class) {
                if (sInstance == null) {
                    sInstance = new GenerateChunksInRenderDistanceWorldUpdateStep();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void update(World world, float delta) {
        int cx = WxCx(world.getViewpointWp().x);
        int cz = WzCz(world.getViewpointWp().z);
        for (int ix = cx - RenderSettingsRegistry.renderDistance; ix < cx + RenderSettingsRegistry.renderDistance; ix++) {
            for (int iz = cz - RenderSettingsRegistry.renderDistance; iz < cz + RenderSettingsRegistry.renderDistance; iz++) {
                // Generate chunk using generating side-effect of World#getChunkAt_G_CP
                world.getChunkAt_G_CP(ix, iz);
            }
        }
    }
}
