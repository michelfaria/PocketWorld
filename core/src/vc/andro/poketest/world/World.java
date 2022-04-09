package vc.andro.poketest.world;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class World {
    public final int width;
    public final int height;

    public final Tile[] tiles;
    public final Array<Entity> entities;

    public World(Tile[] tiles, int width, int height, Array<Entity> entities) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        this.entities = entities;
    }
}
