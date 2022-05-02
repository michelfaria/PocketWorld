package vc.andro.poketest.world;

import vc.andro.poketest.registry.GeneralSettingsRegistry;
import vc.andro.poketest.registry.RenderSettingsRegistry;

import static vc.andro.poketest.world.World.WxCx;
import static vc.andro.poketest.world.World.WzCz;

public class GenerateChunksInRenderDistanceWorldUpdateStep implements WorldUpdateStep {
    @Override
    public void update(World world, float delta) {
        if (GeneralSettingsRegistry.debugChunkGenerateOnKeyPress) {
            return;
        }

        int cx = WxCx(world.getViewpointWp().x);
        int cz = WzCz(world.getViewpointWp().z);
        for (int ix = cx - RenderSettingsRegistry.renderDistance; ix < cx + RenderSettingsRegistry.renderDistance; ix++) {
            for (int iz = cz - RenderSettingsRegistry.renderDistance; iz < cz + RenderSettingsRegistry.renderDistance; iz++) {
                world.generateChunkAt_CP_IfNotExists(ix, iz);
            }
        }
    }
}
