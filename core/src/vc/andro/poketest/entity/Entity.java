package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.Nullable;

import static vc.andro.poketest.PocketWorld.TILE_SIZE;

public class Entity {
    private float wx;
    private float wy;
    private float wz;

    protected Vector2 dimensions;
    protected Array<EntityDecal> decals;

    public Entity() {
        this(new Vector2(1, 1));
    }

    public Entity(Vector2 dimensions) {
        this.dimensions = dimensions;
        decals = new Array<>(EntityDecal.class);
    }

    protected void addDecal(EntityDecal eDecal) {
        TextureRegion txReg = eDecal.getTextureRegion();
        eDecal.decal.setDimensions(
                txReg.getRegionWidth() / TILE_SIZE,
                txReg.getRegionHeight() / TILE_SIZE
        );
        eDecal.decal.setRotation(eDecal.yaw, eDecal.pitch, eDecal.roll);
        decals.add(eDecal);
    }

    protected void addDecals(EntityDecal... decals) {
        for (EntityDecal decal : decals) {
            addDecal(decal);
        }
    }

    public void draw(DecalBatch decalBatch) {
        for (EntityDecal eDecal : decals) {
            decalBatch.add(eDecal.decal);
        }
    }

    public void tick() {
    }

    public void update(float delta) {
    }

    public void setPosition(float wx, float wy, float wz) {
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;

        for (EntityDecal eDecal : decals) {
            eDecal.decal.setPosition(
                    wx + eDecal.offsetWx + dimensions.x / 2.0f,
                    wy + eDecal.offsetWy,
                    wz + eDecal.offsetWz + dimensions.y / 2.0f
            );
        }
    }

    public float getWx() {
        return wx;
    }

    public float getWy() {
        return wy;
    }

    public float getWz() {
        return wz;
    }
}
