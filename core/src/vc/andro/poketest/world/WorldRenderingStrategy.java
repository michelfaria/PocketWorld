package vc.andro.poketest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import vc.andro.poketest.PocketCamera;
import vc.andro.poketest.Registry;
import vc.andro.poketest.entity.Entity;

import java.util.Iterator;

import static vc.andro.poketest.world.World.WxCx;
import static vc.andro.poketest.world.World.WzCz;

public class WorldRenderingStrategy implements RenderableProvider {
    private final PocketCamera cam;
    private final World world;

    private float viewpointWx;
    private float viewpointWz;
    private int renderDistanceInChunks = 10;

    public WorldRenderingStrategy(PocketCamera cam, World world) {
        this.cam = cam;
        this.world = world;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        int chunksRendered = 0;
        int cx = WxCx(viewpointWx);
        int cz = WzCz(viewpointWz);
        for (int ix = cx - renderDistanceInChunks; ix < cx + renderDistanceInChunks; ix++) {
            for (int iz = cz - renderDistanceInChunks; iz < cz + renderDistanceInChunks; iz++) {
                Chunk chunk = world.getChunkAt_G_CP(ix, iz);
                chunk.getRenderables(renderables, pool);
                chunksRendered++;
            }
        }
        Registry.debugInfoMap.put("chunksRendered", Integer.toString(chunksRendered));
    }

    public void renderEntities(DecalBatch decalBatch) {
        int rendered = 0;
        for (Entity entity : world.getEntities()) {
            entity.draw(decalBatch);
            rendered++;
        }
        Registry.debugInfoMap.put("entitiesRendered", Integer.toString(rendered));
    }

    public void setCameraPosition_WP(float wx, float wz) {
        viewpointWx = wx;
        viewpointWz = wz;
        unloadChunksOutsideOfRenderDistance();
        deleteEntitiesOutsideOfRenderDistance();
    }

    private boolean isChunkOutsideOfRenderDistance(Chunk chunk) {
        return isChunkOutsideOfRenderDistance_CP(chunk.cx, chunk.cz);
    }

    private boolean isChunkOutsideOfRenderDistance_CP(int cx, int cz) {
        return Math.abs(WxCx(viewpointWx) - cx) > renderDistanceInChunks || Math.abs(WzCz(viewpointWz) - cz) > renderDistanceInChunks;
    }

    private void unloadChunksOutsideOfRenderDistance() {
        for (IntMap<Chunk> yMap : world.getChunks().map.values()) {
            Iterator<Chunk> iterChunk = yMap.values().iterator();
            while (iterChunk.hasNext()) {
                Chunk chunk = iterChunk.next();
                if (isChunkOutsideOfRenderDistance(chunk)) {
                    iterChunk.remove();
                    Gdx.app.log("World", "DELETED chunk at (" + chunk.cx + "," + chunk.cz + ")");
                }
            }
        }
    }

    private void deleteEntitiesOutsideOfRenderDistance() {
        for (Array.ArrayIterator<Entity> iterator = world.getEntities().iterator(); iterator.hasNext(); ) {
            Entity entity = iterator.next();
            int entityCx = WxCx(entity.getWx());
            int entityCz = WzCz(entity.getWz());
            if (isChunkOutsideOfRenderDistance_CP(entityCx, entityCz)) {
                iterator.remove();
                Gdx.app.log("World", "DELETED entity:" + entity);
            }
        }
    }
}
