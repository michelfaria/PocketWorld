package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Entity {
    public float worldX;
    public float y;
    public float worldZ;
    public final String spriteId;

    public Entity(String spriteId) {
        this.spriteId = spriteId;
    }

    public void draw(SpriteBatch spriteBatch) {
        TextureRegion sprite = AtlasUtil.findRegion(PokeTest.assetManager.get(Assets.entityAtlas), spriteId);
        draw(spriteBatch, sprite);
    }

    protected void draw(SpriteBatch spriteBatch, TextureRegion sprite) {
        spriteBatch.draw(
                sprite,
                worldX * TILE_SIZE,
                worldZ * TILE_SIZE
        );
    }

    public void tick() {
    }

    public void update(float delta) {
    }
}
