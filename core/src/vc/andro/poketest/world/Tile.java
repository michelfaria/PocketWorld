package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Tile {

    private final TileType type;
    private final float altitude;
    private final int x;
    private final int y;

    public Tile(TileType type, float altitude, int x, int y) {
        this.type = type;
        this.altitude = altitude;
        this.x = x;
        this.y = y;
    }

    public void draw(SpriteBatch spriteBatch, TextureAtlas textureAtlas) {
        TextureRegion region = textureAtlas.findRegion(type.spriteId);
        spriteBatch.draw(
                region,
                x * TILE_SIZE,
                y * TILE_SIZE
        );
    }

    public float getAltitude() {
        return altitude;
    }

    public TileType getType() {
        return type;
    }
}
