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

public final class VoxelType {

    public @NotNull CubicGroup<String> textureRegionIds;
    public @NotNull CubicGroup<TextureRegion> textureRegions;
    public @NotNull CubicGroup<UVCalculationStrategy> uvCalculationStrategies;
    public @NotNull FaceGenerationStrategy faceGenerationStrategy;
    public boolean transparent;
    public @Nullable SlopeType slopeType;

    public VoxelType(String spriteId) {
        this(spriteId, spriteId, spriteId, spriteId, spriteId, spriteId);
    }

    public VoxelType(String spriteId, FaceGenerationStrategy faceGenerationStrategy) {
        this(spriteId, spriteId, spriteId, spriteId, spriteId, spriteId, faceGenerationStrategy);
    }

    public VoxelType(String topSpriteId,
                     String northSpriteId,
                     String southSpriteId,
                     String westSpriteId,
                     String eastSpriteId,
                     String bottomSpriteId) {
        this(topSpriteId, northSpriteId, southSpriteId, westSpriteId, eastSpriteId, bottomSpriteId,
                CubeFaceGenerationStrategy.getInstance());
    }

    public VoxelType(String topSpriteId,
                     String northSpriteId,
                     String southSpriteId,
                     String westSpriteId,
                     String eastSpriteId,
                     String bottomSpriteId,
                     @NotNull FaceGenerationStrategy faceGenerationStrategy) {
        this.textureRegionIds = new CubicGroup<>(topSpriteId, bottomSpriteId, westSpriteId, eastSpriteId, northSpriteId, southSpriteId);

        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.tileAtlas);

        this.textureRegions = textureRegionIds.map((id, $) -> {
            if (id != null) {
                return AtlasUtil.findRegion(atlas, id);
            }
            return null;
        });

        this.uvCalculationStrategies =
                this.textureRegions.map((region, face) ->
                        region == null
                                ? NullUVCalculationStrategy.getInstance()
                                : (region.getRegionWidth() > PPU || region.getRegionHeight() > PPU)
                                ? BigTextureUVCalculationStrategy.getInstance()
                                : DefaultUVCalculationStrategy.getInstance());

        this.faceGenerationStrategy = faceGenerationStrategy;
    }

    @Deprecated
    public void setSlopeType(@Nullable SlopeType slopeType) {
        this.slopeType = slopeType;
        if (slopeType != null) {
            transparent = true;
        }
    }
}
