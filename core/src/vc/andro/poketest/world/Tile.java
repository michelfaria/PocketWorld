package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.DrawingContext;
import vc.andro.poketest.PokeTest;

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

    public void draw(DrawingContext context) {
        TextureRegion region = PokeTest.getTextureAtlas().findRegion(type.spriteId);
        context.spriteBatch.draw(
                region,
                x * TILE_SIZE,
                y * TILE_SIZE,
                0f,
                0f,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1f,
                1f,
                context.viewRotation.getValue()
        );
    }

    public float getAltitude() {
        return altitude;
    }

    public TileType getType() {
        return type;
    }
}
