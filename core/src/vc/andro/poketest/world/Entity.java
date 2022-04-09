package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.DrawingContext;
import vc.andro.poketest.PokeTest;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Entity {
    public float x;
    public float y;
    public final String typeName;
    public final TextureRegion texture;

    public Entity(String typeName, TextureRegion texture) {
        this.typeName = typeName;
        this.texture = texture;
    }

    public void draw(DrawingContext context) {
        context.spriteBatch.draw(
                texture,
                x * TILE_SIZE,
                y * TILE_SIZE,
                0f,
                0f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                1f,
                1f,
                -context.viewRotation.getValue()
        );
    }
}
