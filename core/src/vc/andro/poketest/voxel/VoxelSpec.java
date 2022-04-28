package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.voxel.rendering.faces.FaceGenerationStrategy;
import vc.andro.poketest.voxel.rendering.faces.NeoFaceGenerationStrategy;
import vc.andro.poketest.voxel.rendering.uv.BigTextureUVCalculationStrategy;
import vc.andro.poketest.voxel.rendering.uv.DefaultUVCalculationStrategy;
import vc.andro.poketest.voxel.rendering.uv.NullUVCalculationStrategy;
import vc.andro.poketest.voxel.rendering.uv.UVCalculationStrategy;
import vc.andro.poketest.world.chunk.render.VoxelRenderingStrategy;

import static vc.andro.poketest.PocketWorld.PPU;

public final class VoxelSpec {

    public final @Nullable CubicGroup<String>                textureRegionIds;
    public final @Nullable CubicGroup<TextureRegion>         textureRegions;
    public final @Nullable CubicGroup<UVCalculationStrategy> uvCalculationStrategies;
    public final @Nullable FaceGenerationStrategy            faceGenerationStrategy;
    public final           boolean                           transparent;
    public final           boolean                           canBeSloped;
    public final @Nullable VoxelRenderingStrategy            voxelRenderingStrategy;

    public VoxelSpec(
            @Nullable CubicGroup<String> textureRegionIds, @Nullable FaceGenerationStrategy faceGenerationStrategy,
            boolean transparent, boolean canBeSloped, @Nullable VoxelRenderingStrategy voxelRenderingStrategy) {
        this.textureRegionIds = textureRegionIds;
        this.faceGenerationStrategy = faceGenerationStrategy != null ? faceGenerationStrategy : NeoFaceGenerationStrategy.getInstance();
        this.transparent = transparent;
        this.canBeSloped = canBeSloped;
        this.voxelRenderingStrategy = voxelRenderingStrategy;

        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.tileAtlas);

        if (textureRegionIds != null) {
            textureRegions = textureRegionIds.map((id, $) -> {
                if (id != null) {
                    return AtlasUtil.findRegion(atlas, id);
                }
                return null;
            });

            uvCalculationStrategies =
                    textureRegions.map((region, face) ->
                            region == null
                                    ? NullUVCalculationStrategy.getInstance()
                                    : (region.getRegionWidth() > PPU || region.getRegionHeight() > PPU)
                                    ? BigTextureUVCalculationStrategy.getInstance()
                                    : DefaultUVCalculationStrategy.getInstance());
        } else {
            textureRegions = null;
            uvCalculationStrategies = null;
        }
    }
}
