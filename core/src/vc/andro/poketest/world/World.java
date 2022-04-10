package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.Tile;

public class World {
    public final WorldBase worldBase;
    public final Tile[][] tiles;
    public final Array<Entity> entities;

    public World(WorldBase worldBase, Tile[][] tiles) {
        this.worldBase = worldBase;
        this.tiles = tiles;

        entities = new Array<>(Entity.class);
    }

    public void addEntities(Array<Entity> entities) {
        for (Entity entity : entities) {
            this.entities.add(entity);
        }
    }

    public int getHeight() {
        return worldBase.getHeight();
    }

    public int getWidth() {
        return worldBase.getWidth();
    }

    public int getDepth() {
        return worldBase.getDepth();
    }
}
