package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Entity {
    public float x;
    public float y;
    public final String gameId;
    public final String spriteId;

    public Entity(String gameId, String spriteId) {
        this.gameId = gameId;
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
