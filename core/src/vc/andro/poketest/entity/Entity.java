package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Entity {
    public float x;
    public float y;
    public final String spriteId;

    public Entity(String spriteId) {
        this.spriteId = spriteId;
    }

    public void draw(SpriteBatch spriteBatch) {
        TextureRegion sprite = AtlasUtil.findRegion(PokeTest.assetManager.get(Assets.textureAtlas), spriteId);
        spriteBatch.draw(
                sprite,
                x * TILE_SIZE,
                y * TILE_SIZE
        );
    }

    public void tick() {
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
