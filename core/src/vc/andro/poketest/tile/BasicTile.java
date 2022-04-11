package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.world.World;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class BasicTile {

    protected final World world;
    protected final TileType type;
    protected final int x;
    protected final int y;
    protected final float altitude;

    protected String spriteId;

    public BasicTile(World world, TileType type, int x, int y, float altitude) {
        this.world = world;
        this.type = type;
        this.altitude = altitude;
        this.x = x;
        this.y = y;

        spriteId = type.defaultSpriteId;
    }

    public void draw(SpriteBatch spriteBatch) {
        draw(spriteBatch, x, y);
    }

    protected void draw(SpriteBatch spriteBatch, int atX, int atY) {
        TextureRegion region = AtlasUtil.findRegion(PokeTest.assetManager.get(Assets.textureAtlas), spriteId);
        spriteBatch.draw(
                region,
                atX * TILE_SIZE,
                atY * TILE_SIZE
        );
    }

    public float getAltitude() {
        return altitude;
    }

    public TileType getType() {
        return type;
    }

    public void doTileUpdate() {
        world.propagateTileUpdate(this);
    }

    public void receiveTileUpdate(BasicTile updateOrigin) {
    }

    public void tick() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean canPlayerWalkOnIt() {
        return type.canPlayerWalkOnIt;
    }
}
