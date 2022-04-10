package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Entity {
    public float x;
    public float y;
    public final String typeName;
    public final String spriteId;

    public Entity(String typeName, String spriteId) {
        this.typeName = typeName;
        this.spriteId = spriteId;
    }

    public void draw(SpriteBatch spriteBatch) {
        TextureRegion sprite = PokeTest.assetManager.get(Assets.textureAtlas).findRegion(spriteId);
        spriteBatch.draw(
                sprite,
                x * TILE_SIZE,
                y * TILE_SIZE
        );
    }
}
