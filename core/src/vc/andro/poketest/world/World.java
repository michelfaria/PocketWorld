package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicTile;

public class World {
    private final WorldBase base;
    private final BasicTile[][] tiles;
    private final Array<Entity> entities;

    public World(WorldBase base) {
        this.base = base;

        tiles = new BasicTile[base.getWidth()][base.getHeight()];
        entities = new Array<>(Entity.class);
    }

    public void addEntity(Entity e) {
        this.entities.add(e);
    }

    public void addEntities(Array<Entity> entities) {
        for (Entity entity : entities) {
            this.entities.add(entity);
        }
    }

    public WorldBase getBase() {
        return base;
    }

    public int getHeight() {
        return base.getHeight();
    }

    public int getWidth() {
        return base.getWidth();
    }

    public void updateAllTiles() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                getTile(x, y).doTileUpdate();
            }
        }
    }

    public void propagateTileUpdate(BasicTile updateOrigin) {
        var ox = updateOrigin.getX();
        var oy = updateOrigin.getY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == dy || ox + dx < 0 || ox + dx >= getWidth() || oy + dy < 0 || oy + dy >= getHeight()) {
                    continue;
                }
                var tile = getTile(ox + dx, oy + dy);
                tile.receiveTileUpdate(updateOrigin);
            }
        }
    }

    public void setTile(int x, int y, BasicTile tile) {
        tiles[x][y] = tile;
    }

    public BasicTile getTile(int x, int y) {
        return tiles[x][y];
    }

    public BasicTile[][] getTiles() {
        return tiles;
    }

    public Array<Entity> getEntities() {
        return entities;
    }
}
