package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import org.jetbrains.annotations.NotNull;

class EntityDecal {
    @NotNull final Decal decal;

    float yaw;
    float pitch;
    float roll;
    float offsetWx;
    float offsetWy;
    float offsetWz;

    public EntityDecal(@NotNull Decal decal) {
        this.decal = decal;
    }

    TextureRegion getTextureRegion() {
        return decal.getTextureRegion();
    }
}
