package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PokeTest;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.VertexArray;
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
    public boolean transparent;

    public TileType type;
    public TextureRegion textureRegion;

    public BasicTile(TileType type) {
        this.type = type;
        setSprite(type.defaultSpriteId);
    }

    public void draw(SpriteBatch spriteBatch) {
        draw(spriteBatch, worldX, worldZ);
    }

    protected void draw(SpriteBatch spriteBatch, int atX, int atZ) {
        spriteBatch.draw(textureRegion, atX * TILE_SIZE, atZ * TILE_SIZE);
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
        textureRegion = AtlasUtil.findRegion(PokeTest.assetManager.get(Assets.tileAtlas), spriteId);
    }

    public void createRightVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ,         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + 1,          // y
                worldZ + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + 1,          // y
                worldZ,         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
    }

    public void createFrontVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,         // x
                y,              // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + 1,          // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX,         // x
                y + 1,          // y
                worldZ,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
    }

    public void createBackVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,         // x
                y,              // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX,         // x
                y + 1,          // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y + 1,          // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
    }

    public void createTopVertices(VertexArray vertices) {
        // northwest
        vertices.addVertex(
                worldX,                    // x
                y + 1,                     // y
                worldZ,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV()      // v
        );
        // northeast
        vertices.addVertex(
                worldX + 1,                // x
                y + 1,                     // y
                worldZ,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV()      // v
        );
        // southeast
        vertices.addVertex(
                worldX + 1,                // x
                y + 1,                     // y
                worldZ + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV2()       // v
        );
        // southwest
        vertices.addVertex(
                worldX,                    // x
                y + 1,                     // y
                worldZ + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV2()       // v
        );
    }

    public void createBottomVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,         // x
                y,              // y
                worldZ,         // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX,         // x
                y,              // y
                worldZ + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,     // x
                y,              // y
                worldZ + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex(
                worldX + 1,    // x
                y,             // y
                worldZ,        // z
                0,             // normal x
                -1,            //        y
                0,             //        z
                0,             // u
                0);            // v
    }

    public void createLeftVertices(VertexArray vertices) {
        vertices.addVertex(
                worldX,        // x
                y,             // y
                worldZ,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y + 1,         // y
                worldZ,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y + 1,         // y
                worldZ + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex(
                worldX,        // x
                y,             // y
                worldZ + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
    }
}
