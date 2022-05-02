package vc.andro.poketest.voxel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.util.CubicGroup;
import vc.andro.poketest.world.chunk.render.VoxelRenderer;

public class Voxel {

    private final @Nullable CubicGroup<String>        textureRegionIds;
    private final @Nullable CubicGroup<TextureRegion> textureRegions;
    private final           boolean                   transparent;
    private final           boolean                   canBeSloped;
    private final @Nullable VoxelRenderer             voxelRenderer;
    private final           boolean                   destroyedBySloping;

    public Voxel(
            @Nullable CubicGroup<String> textureRegionIds,
            @Nullable VoxelRenderer voxelRenderer, boolean transparent, boolean canBeSloped,
            boolean destroyedBySloping) {
        this.textureRegionIds = textureRegionIds;
        this.transparent = transparent;
        this.canBeSloped = canBeSloped;
        this.voxelRenderer = voxelRenderer;
        this.destroyedBySloping = destroyedBySloping;

        TextureAtlas atlas = PocketWorld.getAssetManager().get(Assets.tileAtlas);

        if (textureRegionIds != null) {
            textureRegions = textureRegionIds.map((id, $) -> {
                if (id != null) {
                    return AtlasUtil.findRegion(atlas, id);
                }
                return null;
            });
        } else {
            textureRegions = null;
        }
    }

    @Nullable
    public CubicGroup<String> getTextureRegionIds() {
        return textureRegionIds;
    }

    @Nullable
    public CubicGroup<TextureRegion> getTextureRegions() {
        return textureRegions;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public boolean isCanBeSloped() {
        return canBeSloped;
    }

    @Nullable
    public VoxelRenderer getVoxelRenderer() {
        return voxelRenderer;
    }

    public boolean isDestroyedBySloping() {
        return destroyedBySloping;
    }
}
