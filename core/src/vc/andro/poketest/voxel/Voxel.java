package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.VertexArray;
import vc.andro.poketest.world.World;

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

    Voxel() {
    }

    public void init(VoxelType type) {
        this.type = type;
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
    }

    public void storePosition(Chunk chunk, int lx, int wy, int lz) {
        world = chunk.world;
        this.chunk = chunk;
        wx = LxWx(chunk.cx, lx);
        this.wy = wy;
        wz = LzWz(chunk.cz, lz);
        this.lx = lx;
        this.lz = lz;
    }

    public void doTileUpdate() {
        getWorld().broadcastTileUpdateToAdjacentTiles(this);
    }

    public void receiveTileUpdate(Voxel updateOrigin) {
    }

    public void tick() {
    }


    public void createTopVertices(VertexArray vertices) {
        type.faceGenerationStrategy.createTopVertices(this, vertices);
    }

    public void createBottomVertices(VertexArray vertices) {
        type.faceGenerationStrategy.createBottomVertices(this, vertices);
    }

    public void createNorthVertices(VertexArray vertices) {
        type.faceGenerationStrategy.createNorthVertices(this, vertices);
    }

    public void createSouthVertices(VertexArray vertices) {
        type.faceGenerationStrategy.createSouthVertices(this, vertices);
    }

    public void createWestVertices(VertexArray vertices) {
        type.faceGenerationStrategy.createWestVertices(this, vertices);
    }

    public void createEastVertices(VertexArray vertices) {
        type.faceGenerationStrategy.createEastVertices(this, vertices);
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
        return getType().textureRegions.getFace(whichFace);
    }

    public int getLx() {
        return lx;
    }

    public int getLz() {
        return lz;
    }

    public boolean isTransparent() {
        return type.transparent;
    }

    public VoxelType getType() {
        return type;
    }

    public CubicGroup<UVCalculationStrategy> getUvCalculationStrategies() {
        return getType().uvCalculationStrategies;
    }
}
