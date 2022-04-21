package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static vc.andro.poketest.PocketWorld.PPU;

public class Entity {
    private float wx;
    private float wy;
    private float wz;

    public final Vector3 dimensions;
    protected Array<EntityDecal> decals;

    public Entity() {
        this(new Vector3(1.0f, 1.0f, 1.0f));
    }

    public Entity(Vector3 dimensions) {
        this.dimensions = dimensions;
        decals = new Array<>(EntityDecal.class);
    }

    protected void addDecal(EntityDecal eDecal) {
        TextureRegion txReg = eDecal.getTextureRegion();
        eDecal.decal.setDimensions(
                txReg.getRegionWidth() / PPU,
                txReg.getRegionHeight() / PPU
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
