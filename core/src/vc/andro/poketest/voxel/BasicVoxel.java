package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.VertexArray;
import vc.andro.poketest.world.World;

import static vc.andro.poketest.PocketWorld.PPU;
import static vc.andro.poketest.world.World.LxWx;
import static vc.andro.poketest.world.World.LzWz;

public class BasicVoxel {

    private World world;
    private Chunk chunk;
    private int wx;
    private int wy;
    private int wz;
    private int lx;
    private int lz;
    private boolean transparent;
    public VoxelType type;
    private CubicGroup<TextureRegion> textureRegions;
    private CubicGroup<UVCalculationStrategy> uvCalculationStrategies;

    public BasicVoxel(VoxelType type) {
        this.type = type;
        setTextures(type.textureRegionIds);
    }

    public void storePosition(Chunk chunk, int lx, int wy, int lz) {
        world = chunk.world;
        this.chunk = chunk;
        wx = LxWx(chunk.cx, lx);
        this.wy = wy;
        wz = LzWz(chunk.cz, lz);
        this.lx = lx;
        this.lz = lz;

        uvCalculationStrategies.forEach((strategy, $) -> {
            if (strategy != null) {
                strategy.refresh();
            }
        });
    }

    public void doTileUpdate() {
        getWorld().broadcastTileUpdateToAdjacentTiles(this);
    }

    public void receiveTileUpdate(BasicVoxel updateOrigin) {
    }

    public void tick() {
    }

    public void setTextures(CubicGroup<String> textureRegionIds) {
        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.tileAtlas);
        textureRegions = textureRegionIds.map((id, $) -> {
            if (id != null) {
                return AtlasUtil.findRegion(atlas, id);
            }
            return null;
        });
        uvCalculationStrategies = textureRegions.map((region, face) -> {
            if (region == null) {
                return null;
            }
            if (region.getRegionWidth() > PPU || region.getRegionHeight() > PPU) {
                return new BigTextureUVCalculationStrategy(this, face);
            }
            return new DefaultUVCalculationStrategy(this, face);
        });
    }

    public void createTopVertices(VertexArray vertices) {
        // northwest
        vertices.addVertex8f(
                getWx(),                    // x
                getWy() + 1,                     // y
                getWz(),                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU(),      // u
                uvCalculationStrategies.top.getV()      // v
        );
        // northeast
        vertices.addVertex8f(
                getWx() + 1,                // x
                getWy() + 1,                     // y
                getWz(),                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU2(),     // u
                uvCalculationStrategies.top.getV()      // v
        );
        // southeast
        vertices.addVertex8f(
                getWx() + 1,                // x
                getWy() + 1,                     // y
                getWz() + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU2(),     // u
                uvCalculationStrategies.top.getV2()      // v
        );
        // southwest
        vertices.addVertex8f(
                getWx(),                    // x
                getWy() + 1,                     // y
                getWz() + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU(),      // u
                uvCalculationStrategies.top.getV2()      // v
        );
    }

    public void createRightVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz(),         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz() + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz(),         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                0,              // u
                0);             // v
    }

    public void createFrontVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy() + 1,          // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                0,              // u
                0);             // v
    }

    public void createBackVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy() + 1,          // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                0,              // u
                0);             // v
    }

    public void createBottomVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz(),         // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                0,              // u
                0);             // v
        vertices.addVertex8f(
                getWx() + 1,    // x
                getWy(),             // y
                getWz(),        // z
                0,             // normal x
                -1,            //        y
                0,             //        z
                0,             // u
                0);            // v
    }

    public void createLeftVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),        // x
                getWy(),             // y
                getWz(),        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy() + 1,         // y
                getWz(),        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy() + 1,         // y
                getWz() + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy(),             // y
                getWz() + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                0,             // u
                0);            // v
    }

    public World getWorld() {
        return world;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public int getWx() {
        return wx;
    }

    public int getWy() {
        return wy;
    }

    public int getWz() {
        return wz;
    }

    public TextureRegion getTextureRegion(CubicGroup.Face whichFace) {
        return textureRegions.getFace(whichFace);
    }

    public int getLx() {
        return lx;
    }

    public int getLz() {
        return lz;
    }

    public boolean isTransparent() {
        return transparent;
    }

    protected void setTransparent(boolean newValue) {
        transparent = newValue;
    }
}
