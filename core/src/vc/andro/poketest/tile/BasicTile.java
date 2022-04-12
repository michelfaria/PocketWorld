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
    public Integer worldY;
    public Integer chunkLocalX;
    public Integer chunkLocalY;

    public float altitude;
    public TileType type;
    public TextureRegion texture;

    public BasicTile(TileType type, float altitude) {
        this.type = type;
        this.altitude = altitude;
        setSprite(type.defaultSpriteId);
    }

    public void draw(SpriteBatch spriteBatch) {
        draw(spriteBatch, worldX, worldY);
    }

    protected void draw(SpriteBatch spriteBatch, int atX, int atY) {
        spriteBatch.draw(texture, atX * TILE_SIZE, atY * TILE_SIZE);
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

    @Override
    public String toString() {
        return "BasicTile{" +
                "world=" + world +
                ", chunk=" + chunk +
                ", worldX=" + worldX +
                ", worldY=" + worldY +
                ", chunkLocalX=" + chunkLocalX +
                ", chunkLocalY=" + chunkLocalY +
                ", altitude=" + altitude +
                ", type=" + type +
                ", texture=" + texture +
                '}';
    }
}
