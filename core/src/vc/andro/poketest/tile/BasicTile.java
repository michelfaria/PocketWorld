package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.World;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class BasicTile {

    public World world;
    public Chunk chunk;
    public Integer worldX;
    public Integer worldZ;
    public Integer chunkLocalX;
    public Integer y;
    public Integer chunkLocalZ;
    public boolean isTransparent = false;

    public TileType type;
    public TextureRegion texture;

    public BasicTile(TileType type) {
        this.type = type;
        setSprite(type.defaultSpriteId);
    }

    public void draw(SpriteBatch spriteBatch) {
        draw(spriteBatch, worldX, worldZ);
    }

    protected void draw(SpriteBatch spriteBatch, int atX, int atZ) {
        spriteBatch.draw(texture, atX * TILE_SIZE, atZ * TILE_SIZE);
    }

    public void doTileUpdate() {
        world.broadcastTileUpdateToAdjacentTiles(this);
    }

    public void receiveTileUpdate(BasicTile updateOrigin) {
    }

    public void tick() {
    }

    public boolean canPlayerWalkOnIt() {
        return type.canPlayerWalkOnIt;
    }

    public void setSprite(String spriteId) {
        texture = AtlasUtil.findRegion(PokeTest.assetManager.get(Assets.textureAtlas), spriteId);
    }
}
