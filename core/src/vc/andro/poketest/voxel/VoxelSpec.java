package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.util.CubicGroup;

import static vc.andro.poketest.PocketWorld.PPU;

public final class VoxelSpec {

    public final @NotNull CubicGroup<String> textureRegionIds;
    public final @NotNull CubicGroup<TextureRegion> textureRegions;
    public final @NotNull CubicGroup<UVCalculationStrategy> uvCalculationStrategies;
    public final @NotNull FaceGenerationStrategy faceGenerationStrategy;
    public final boolean transparent;
    public final boolean canBeSloped;

    public VoxelSpec(
            @NotNull CubicGroup<String> textureRegionIds, @Nullable FaceGenerationStrategy faceGenerationStrategy,
            boolean transparent, boolean canBeSloped) {
        this.textureRegionIds = textureRegionIds;
        this.faceGenerationStrategy = faceGenerationStrategy != null ? faceGenerationStrategy : NeoFaceGenerationStrategy.getInstance();
        this.transparent = transparent;
        this.canBeSloped = canBeSloped;

        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.tileAtlas);

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
    }
}
