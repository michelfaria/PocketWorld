package vc.andro.poketest.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;

public class Duocal {
    private final Array<Decal> decals;

    public Duocal(TextureRegion textureRegion, boolean hasTrasparency) {
        decals = new Array<>(Decal.class);
        for (int i = 0; i < 2; i++) {
            decals.add(Decal.newDecal(textureRegion, hasTrasparency));
        }
    }

    public void setDimensions(float width, float height) {
        for (Decal decal : decals) {
            decal.setDimensions(width, height);
        }
    }

    public void setRotation(float yaw, float pitch, float roll) {
        for (int i = 0; i < decals.size; i++) {
            Decal d = decals.get(i);
            d.setRotation(yaw + (i * 90), pitch, roll);
        }
    }

    public void addToBatch(DecalBatch decalBatch) {
        for (Decal decal : decals) {
            decalBatch.add(decal);
        }
    }

    public void setPosition(float x, float y, float z) {
        for (Decal decal : decals) {
            decal.setPosition(x, y, z);
        }
    }
}
