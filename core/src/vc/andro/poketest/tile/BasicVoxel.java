package vc.andro.poketest.tile;

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
    public Integer wy;
    public Integer wz;
    public Integer lx;
    public Integer lz;
    public boolean transparent;
    public boolean bigTexture;

    public VoxelType type;
    public TextureRegion textureRegion;

    public BasicVoxel(VoxelType type) {
        this.type = type;
        setSprite(type.defaultSpriteId);
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
        if (textureRegion.getRegionWidth() > PPU) {
            bigTexture = true;
        }
    }

    private float getTopU() {
        if (bigTexture) {
            int tilesW = (int) (textureRegion.getRegionWidth() / PPU);
            int partX = Math.abs(wx) % tilesW;
            float uDiff = textureRegion.getU2() - textureRegion.getU();
            float uPerTile = uDiff / tilesW;
            float u = textureRegion.getU() + uPerTile * partX;
            return u;
        } else {
            return textureRegion.getU();
        }
    }

    private float getTopU2() {
        if (bigTexture) {
            int tilesW = (int) (textureRegion.getRegionWidth() / PPU);
            int partX = Math.abs(wx) % tilesW;
            float uDiff = textureRegion.getU2() - textureRegion.getU();
            float uPerTile = uDiff / tilesW;
            float u2 = textureRegion.getU() + (uPerTile * partX) + uPerTile;
            return u2;
        } else {
            return textureRegion.getU2();
        }
    }

    private float getTopV() {
        if (bigTexture) {
            int tilesH = (int) (textureRegion.getRegionHeight() / PPU);
            int partZ = Math.abs(wz) % tilesH;
            float vDiff = textureRegion.getV2() - textureRegion.getV();
            float vPerTile = vDiff / tilesH;
            float v = textureRegion.getV() + vPerTile * partZ;
            return v;
        } else {
            return textureRegion.getV();
        }
    }

    private float getTopV2() {
        if (bigTexture) {
            int tilesH = (int) (textureRegion.getRegionHeight() / PPU);
            int partZ = (Math.abs(wz) + 1) % tilesH;
            float vDiff = textureRegion.getV2() - textureRegion.getV();
            float vPerTile = vDiff / tilesH;
            float v2 = textureRegion.getV() + (vPerTile * partZ) + vPerTile;
            return v2;
        } else {
            return textureRegion.getV2();
        }
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
                getTopU(),      // u
                getTopV()      // v
        );
        // northeast
        vertices.addVertex8f(
                wx + 1,                // x
                wy + 1,                     // y
                wz,                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTopU2(),     // u
                getTopV()      // v
        );
        // southeast
        vertices.addVertex8f(
                wx + 1,                // x
                wy + 1,                     // y
                wz + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTopU2(),     // u
                getTopV2()      // v
        );
        // southwest
        vertices.addVertex8f(
                wx,                    // x
                wy + 1,                     // y
                wz + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                getTopU(),      // u
                getTopV2()      // v
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
