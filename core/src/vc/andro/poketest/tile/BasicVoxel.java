package vc.andro.poketest.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.VertexArray;
import vc.andro.poketest.world.World;

import static vc.andro.poketest.PocketWorld.PPU;

public class BasicVoxel {

    public World world;
    public Chunk chunk;
    public Integer wx;
    public Integer wz;
    public Integer cx;
    public Integer wy;
    public Integer lz;
    public boolean transparent;

    public VoxelType type;
    public TextureRegion textureRegion;

    public BasicVoxel(VoxelType type) {
        this.type = type;
        setSprite(type.defaultSpriteId);
    }

    public void draw(SpriteBatch spriteBatch) {
        draw(spriteBatch, wx, wz);
    }

    protected void draw(SpriteBatch spriteBatch, int atX, int atZ) {
        spriteBatch.draw(textureRegion, atX * PPU, atZ * PPU);
    }

    public void doTileUpdate() {
        world.broadcastTileUpdateToAdjacentTiles(this);
    }

    public void receiveTileUpdate(BasicVoxel updateOrigin) {
    }

    public void tick() {
    }

    public boolean canPlayerWalkOnIt() {
        return type.canPlayerWalkOnIt;
    }

    public void setSprite(String spriteId) {
        textureRegion = AtlasUtil.findRegion(PocketWorld.assetManager.get(Assets.tileAtlas), spriteId);
    }

    public void createTopVertices(VertexArray vertices) {
        // northwest
        vertices.addVertex8f(
                wx,                    // x
                wy + 1,                     // y
                wz,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV()      // v
        );
        // northeast
        vertices.addVertex8f(
                wx + 1,                // x
                wy + 1,                     // y
                wz,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV()      // v
        );
        // southeast
        vertices.addVertex8f(
                wx + 1,                // x
                wy + 1,                     // y
                wz + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU2(),     // u
                textureRegion.getV2()       // v
        );
        // southwest
        vertices.addVertex8f(
                wx,                    // x
                wy + 1,                     // y
                wz + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                textureRegion.getU(),      // u
                textureRegion.getV2()       // v
        );
    }

    public void createRightVertices(VertexArray vertices) {
        vertices.addVertex8f(
                wx + 1,     // x
                wy,              // y
                wz,         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy,              // y
                wz + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy + 1,          // y
                wz + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy + 1,          // y
                wz,         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
    }

    public void createFrontVertices(VertexArray vertices) {
        vertices.addVertex8f(
                wx,         // x
                wy,              // y
                wz,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy,              // y
                wz,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy + 1,          // y
                wz,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx,         // x
                wy + 1,          // y
                wz,         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
    }

    public void createBackVertices(VertexArray vertices) {
        vertices.addVertex8f(
                wx,         // x
                wy,              // y
                wz + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx,         // x
                wy + 1,          // y
                wz + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy + 1,          // y
                wz + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy,              // y
                wz + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
    }

    public void createBottomVertices(VertexArray vertices) {
        vertices.addVertex8f(
                wx,         // x
                wy,              // y
                wz,         // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx,         // x
                wy,              // y
                wz + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,     // x
                wy,              // y
                wz + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                wx + 1,    // x
                wy,             // y
                wz,        // z
                0,             // normal x
                -1,            //        y
                0,             //        z
                0,             // u
                0);            // v
    }

    public void createLeftVertices(VertexArray vertices) {
        vertices.addVertex8f(
                wx,        // x
                wy,             // y
                wz,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                wx,        // x
                wy + 1,         // y
                wz,        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                wx,        // x
                wy + 1,         // y
                wz + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                wx,        // x
                wy,             // y
                wz + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
    }
}
