package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import vc.andro.poketest.graphics.camera.PocketCamera;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.registry.DebugInfoRegistry;
import vc.andro.poketest.registry.RenderSettingsRegistry;
import vc.andro.poketest.world.chunk.Chunk;

import static vc.andro.poketest.world.World.WxCx;
import static vc.andro.poketest.world.World.WzCz;

public class WorldRenderer implements RenderableProvider {
    private final PocketCamera cam;
    private final World        world;

    public WorldRenderer(PocketCamera cam, World world) {
        this.cam = cam;
        this.world = world;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        int chunksRendered = 0;
        int cx = WxCx(world.getViewpointWp().x);
        int cz = WzCz(world.getViewpointWp().z);
        for (int ix = cx - RenderSettingsRegistry.renderDistance; ix < cx + RenderSettingsRegistry.renderDistance; ix++) {
            for (int iz = cz - RenderSettingsRegistry.renderDistance; iz < cz + RenderSettingsRegistry.renderDistance; iz++) {
                if (!cam.isChunkVisible(ix, iz)) {
                    continue;
                }
                Chunk chunk = world.getChunkAt_CP(ix, iz);
                if (chunk == null) {
                    continue;
                }
                if (chunk.isNotFullyInitialized()) {
                    chunk.fullyInitialize();
                }
                chunk.getChunkRenderingStrategy().getRenderables(renderables, pool);
                chunksRendered++;
            }
        }
        DebugInfoRegistry.chunksRendered = chunksRendered;
    }

    public void renderEntities(DecalBatch decalBatch) {
        int rendered = 0;
        for (Entity entity : world.getEntities()) {
            if (!cam.isVisible(entity)) {
                continue;
            }
            entity.draw(decalBatch);
            rendered++;
        }
        DebugInfoRegistry.entitiesRendered = rendered;
    }
}
