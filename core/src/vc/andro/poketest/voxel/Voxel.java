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
    private FaceGenerationStrategy faceGenerationStrategy;

    Voxel() {
    }

    public void init(VoxelType type) {
        this.type = type;
        setTextures(type.textureRegionIds);
        faceGenerationStrategy = CubeFaceGenerationStrategy.getInstance();
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
        faceGenerationStrategy = null;
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
        faceGenerationStrategy.createTopVertices(this, vertices);
    }

    public void createBottomVertices(VertexArray vertices) {
        faceGenerationStrategy.createBottomVertices(this, vertices);
    }

    public void createNorthVertices(VertexArray vertices) {
        faceGenerationStrategy.createNorthVertices(this, vertices);
    }

    public void createSouthVertices(VertexArray vertices) {
        faceGenerationStrategy.createSouthVertices(this, vertices);
    }

    public void createWestVertices(VertexArray vertices) {
        faceGenerationStrategy.createWestVertices(this, vertices);
    }

    public void createEastVertices(VertexArray vertices) {
        faceGenerationStrategy.createEastVertices(this, vertices);
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

    public CubicGroup<UVCalculationStrategy> getUvCalculationStrategies() {
        return uvCalculationStrategies;
    }
}
