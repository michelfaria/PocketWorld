package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
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

public class Voxel implements Pool.Poolable {

    private World world;
    private Chunk chunk;
    private int wx;
    private int wy;
    private int wz;
    private int lx;
    private int lz;
    private VoxelType type;
    private boolean transparent;
    private CubicGroup<TextureRegion> textureRegions;
    private CubicGroup<UVCalculationStrategy> uvCalculationStrategies;

    Voxel() {
    }

    public void init(VoxelType type) {
        this.type = type;
        setTextures(type.textureRegionIds);
    }

    @Override
    public void reset() {
        world = null;
        chunk = null;
        wx = 0;
        wy = 0;
        wz = 0;
        lx = 0;
        lz = 0;
        type = null;
        transparent = false;
        CubicGroup.pool.free(textureRegions);
        textureRegions = null;
        CubicGroup.pool.free(uvCalculationStrategies);
        uvCalculationStrategies = null;
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
            strategy.refresh();
        });
    }

    public void doTileUpdate() {
        getWorld().broadcastTileUpdateToAdjacentTiles(this);
    }

    public void receiveTileUpdate(Voxel updateOrigin) {
    }

    public void tick() {
    }

    public void setTextures(CubicGroup<String> textureRegionIds) {
        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.tileAtlas);

        textureRegions = textureRegionIds.mapPooled((id, $) -> {
            if (id != null) {
                return AtlasUtil.findRegion(atlas, id);
            }
            return null;
        });

        uvCalculationStrategies = textureRegions.mapPooled((region, face) -> {
            if (region == null) {
                return NullUVCalculationStrategy.getInstance();
            }
            if (region.getRegionWidth() > PPU || region.getRegionHeight() > PPU) {
                return new BigTextureUVCalculationStrategy(this, face);
            }
            return DefaultUVCalculationStrategy.getInstance();
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
                uvCalculationStrategies.top.getU(this, CubicGroup.Face.TOP),      // u
                uvCalculationStrategies.top.getV(this, CubicGroup.Face.TOP)      // v
        );
        // northeast
        vertices.addVertex8f(
                getWx() + 1,                // x
                getWy() + 1,                     // y
                getWz(),                    // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU2(this, CubicGroup.Face.TOP),     // u
                uvCalculationStrategies.top.getV(this, CubicGroup.Face.TOP)      // v
        );
        // southeast
        vertices.addVertex8f(
                getWx() + 1,                // x
                getWy() + 1,                     // y
                getWz() + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU2(this, CubicGroup.Face.TOP),     // u
                uvCalculationStrategies.top.getV2(this, CubicGroup.Face.TOP)      // v
        );
        // southwest
        vertices.addVertex8f(
                getWx(),                    // x
                getWy() + 1,                     // y
                getWz() + 1,                // z
                0,                         // normal x
                1,                         //        y
                0,                         //        z
                uvCalculationStrategies.top.getU(this, CubicGroup.Face.TOP),      // u
                uvCalculationStrategies.top.getV2(this, CubicGroup.Face.TOP)      // v
        );
    }

    public void createEastVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz(),         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                uvCalculationStrategies.east.getU(this, CubicGroup.Face.EAST),              // u
                uvCalculationStrategies.east.getV(this, CubicGroup.Face.EAST));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                uvCalculationStrategies.east.getU2(this, CubicGroup.Face.EAST),              // u
                uvCalculationStrategies.east.getV(this, CubicGroup.Face.EAST));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz() + 1,     // z
                1,              // normal x
                0,              //        y
                0,              //        z
                uvCalculationStrategies.east.getU2(this, CubicGroup.Face.EAST),              // u
                uvCalculationStrategies.east.getV2(this, CubicGroup.Face.EAST));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz(),         // z
                1,              // normal x
                0,              //        y
                0,              //        z
                uvCalculationStrategies.east.getU(this, CubicGroup.Face.EAST),              // u
                uvCalculationStrategies.east.getV2(this, CubicGroup.Face.EAST));             // v
    }

    public void createNorthVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                uvCalculationStrategies.north.getU(this, CubicGroup.Face.NORTH),              // u
                uvCalculationStrategies.north.getV(this, CubicGroup.Face.NORTH));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                uvCalculationStrategies.north.getU2(this, CubicGroup.Face.NORTH),              // u
                uvCalculationStrategies.north.getV(this, CubicGroup.Face.NORTH));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                uvCalculationStrategies.north.getU2(this, CubicGroup.Face.NORTH),              // u
                uvCalculationStrategies.north.getV2(this, CubicGroup.Face.NORTH));             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy() + 1,          // y
                getWz(),         // z
                0,              // normal x
                0,              //        y
                1,              //        z
                uvCalculationStrategies.north.getU(this, CubicGroup.Face.NORTH),              // u
                uvCalculationStrategies.north.getV2(this, CubicGroup.Face.NORTH));             // v
    }

    public void createSouthVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                uvCalculationStrategies.south.getU(this, CubicGroup.Face.SOUTH),              // u
                uvCalculationStrategies.south.getV(this, CubicGroup.Face.SOUTH));             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy() + 1,          // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                uvCalculationStrategies.south.getU(this, CubicGroup.Face.SOUTH),              // u
                uvCalculationStrategies.south.getV2(this, CubicGroup.Face.SOUTH));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy() + 1,          // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                uvCalculationStrategies.south.getU2(this, CubicGroup.Face.SOUTH),              // u
                uvCalculationStrategies.south.getV2(this, CubicGroup.Face.SOUTH));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                0,              //        y
                -1,             //        z
                uvCalculationStrategies.south.getU2(this, CubicGroup.Face.SOUTH),              // u
                uvCalculationStrategies.south.getV(this, CubicGroup.Face.SOUTH));             // v
    }

    public void createBottomVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz(),         // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                uvCalculationStrategies.bottom.getU(this, CubicGroup.Face.BOTTOM),              // u
                uvCalculationStrategies.bottom.getV(this, CubicGroup.Face.BOTTOM));             // v
        vertices.addVertex8f(
                getWx(),         // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                uvCalculationStrategies.bottom.getU(this, CubicGroup.Face.BOTTOM),              // u
                uvCalculationStrategies.bottom.getV2(this, CubicGroup.Face.BOTTOM));             // v
        vertices.addVertex8f(
                getWx() + 1,     // x
                getWy(),              // y
                getWz() + 1,     // z
                0,              // normal x
                -1,             //        y
                0,              //        z
                uvCalculationStrategies.bottom.getU2(this, CubicGroup.Face.BOTTOM),             // u
                uvCalculationStrategies.bottom.getV2(this, CubicGroup.Face.BOTTOM));             // v
        vertices.addVertex8f(
                getWx() + 1,    // x
                getWy(),             // y
                getWz(),        // z
                0,             // normal x
                -1,            //        y
                0,             //        z
                uvCalculationStrategies.bottom.getU2(this, CubicGroup.Face.BOTTOM),             // u
                uvCalculationStrategies.bottom.getV(this, CubicGroup.Face.BOTTOM));            // v
    }

    public void createWestVertices(VertexArray vertices) {
        vertices.addVertex8f(
                getWx(),        // x
                getWy(),             // y
                getWz(),        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                uvCalculationStrategies.west.getU(this, CubicGroup.Face.WEST),             // u
                uvCalculationStrategies.west.getV(this, CubicGroup.Face.WEST));            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy() + 1,         // y
                getWz(),        // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                uvCalculationStrategies.west.getU(this, CubicGroup.Face.WEST),             // u
                uvCalculationStrategies.west.getV2(this, CubicGroup.Face.WEST));            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy() + 1,         // y
                getWz() + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                uvCalculationStrategies.west.getU2(this, CubicGroup.Face.WEST),             // u
                uvCalculationStrategies.west.getV2(this, CubicGroup.Face.WEST));            // v
        vertices.addVertex8f(
                getWx(),        // x
                getWy(),             // y
                getWz() + 1,    // z
                -1,            // normal x
                0,             //        y
                0,             //        z
                uvCalculationStrategies.west.getU2(this, CubicGroup.Face.WEST),             // u
                uvCalculationStrategies.west.getV(this, CubicGroup.Face.WEST));            // v
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

    public VoxelType getType() {
        return type;
    }
}
