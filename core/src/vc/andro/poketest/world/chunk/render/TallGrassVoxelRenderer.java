package vc.andro.poketest.world.chunk.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.util.IndexArray;
import vc.andro.poketest.util.VertexArray;
import vc.andro.poketest.voxel.Voxel;
import vc.andro.poketest.voxel.VoxelAttributes;
import vc.andro.poketest.voxel.rendering.uv.DefaultUVCalculator;
import vc.andro.poketest.voxel.rendering.uv.UVCalculator;
import vc.andro.poketest.world.chunk.Chunk;

public class TallGrassVoxelRenderer implements VoxelRenderer {

    private static volatile TallGrassVoxelRenderer sInstance = null;

    private TallGrassVoxelRenderer() {
        if (sInstance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + TallGrassVoxelRenderer.class.getName()
                            + " class already exists - can't create a new instance.");
        }
    }

    public static TallGrassVoxelRenderer getInstance() {
        if (sInstance == null) {
            synchronized (TallGrassVoxelRenderer.class) {
                if (sInstance == null) {
                    sInstance = new TallGrassVoxelRenderer();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void render(
            @NotNull Chunk chunk, Voxel voxel, int lx, int y, int lz, int wx, int wz,
            @NotNull VertexArray vertexArray8f, @NotNull IndexArray indices, @Nullable VoxelAttributes attrs) {
        CubicGroup<TextureRegion> textureRegions = voxel.getTextureRegions();
        assert textureRegions != null : "Missing grass textures";
        TextureRegion grassTop = textureRegions.getTop();
        assert grassTop != null : "Missing grass top texture";
        TextureRegion grassBottom = textureRegions.getBottom();
        assert grassBottom != null : "Missing grass bottom texture";

        UVCalculator uvCalc = DefaultUVCalculator.getInstance();
        
        vertexArray8f.addVertex8f(
                wx,
                y,
                wz,
                0,
                1,
                0,
                uvCalc.getU(CubicGroup.Face.BOTTOM, voxel, wx, y, wz),
                uvCalc.getV(CubicGroup.Face.BOTTOM, voxel, wx, y, wz)
        );

        vertexArray8f.addVertex8f(
                wx + 1,
                y,
                wz,
                0,
                1,
                0,
                uvCalc.getU2(CubicGroup.Face.BOTTOM, voxel, wx, y, wz),
                uvCalc.getV(CubicGroup.Face.BOTTOM, voxel, wx, y, wz)
        );

        vertexArray8f.addVertex8f(
                wx + 1,
                y,
                wz + 1,
                0,
                1,
                0,
                uvCalc.getU2(CubicGroup.Face.BOTTOM, voxel, wx, y, wz),
                uvCalc.getV2(CubicGroup.Face.BOTTOM, voxel, wx, y, wz)
        );

        vertexArray8f.addVertex8f(
                wx,
                y,
                wz + 1,
                0,
                1,
                0,
                uvCalc.getU(CubicGroup.Face.BOTTOM, voxel, wx, y, wz),
                uvCalc.getV2(CubicGroup.Face.BOTTOM, voxel, wx, y, wz)
        );

        indices.addSquare();

        vertexArray8f.addVertex8f(
                wx,
                y + 0.25f,
                wz,
                0,
                1,
                0,
                uvCalc.getU(CubicGroup.Face.TOP, voxel, wx, y, wz),
                uvCalc.getV(CubicGroup.Face.TOP, voxel, wx, y, wz)
        );

        vertexArray8f.addVertex8f(
                wx + 1,
                y + 0.25f,
                wz,
                0,
                1,
                0,
                uvCalc.getU2(CubicGroup.Face.TOP, voxel, wx, y, wz),
                uvCalc.getV(CubicGroup.Face.TOP, voxel, wx, y, wz)
        );

        vertexArray8f.addVertex8f(
                wx + 1,
                y + 0.25f,
                wz + 1,
                0,
                1,
                0,
                uvCalc.getU2(CubicGroup.Face.TOP, voxel, wx, y, wz),
                uvCalc.getV2(CubicGroup.Face.TOP, voxel, wx, y, wz));

        vertexArray8f.addVertex8f(
                wx,
                y + 0.25f,
                wz + 1,
                0,
                1,
                0,
                uvCalc.getU(CubicGroup.Face.TOP, voxel, wx, y, wz),
                uvCalc.getV2(CubicGroup.Face.TOP, voxel, wx, y, wz));

        indices.addSquare();


    }
}
