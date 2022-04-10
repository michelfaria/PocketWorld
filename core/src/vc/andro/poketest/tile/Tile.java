package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Tile {

    private final TileType type;
    private final int x;
    private final int y;
    private final float altitude;

    private String spriteId;

    public Tile(TileType type, int x, int y, float altitude) {
        this.type = type;
        this.altitude = altitude;
        this.x = x;
        this.y = y;

        spriteId = type.defaultSpriteId;
    }

    public void draw(SpriteBatch spriteBatch) {
        TextureRegion region = PokeTest.assetManager.get(Assets.textureAtlas).findRegion(spriteId);
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

    protected String getSpriteId() {
        return spriteId;
    }

    protected void setSpriteId(String spriteId) {
        this.spriteId = spriteId;
    }
}
