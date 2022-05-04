package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static vc.andro.poketest.PocketWorld.PPU;

public class Entity {
    private final   Vector3            positionWp = new Vector3();
    public final    Vector3            dimensions;
    protected final Array<EntityDecal> decals;

    public Entity() {
        this(new Vector3(1.0f, 1.0f, 1.0f));
    }

    public Entity(Vector3 dimensions) {
        this.dimensions = dimensions;
        decals = new Array<>(EntityDecal.class);
    }

    protected void addDecal(EntityDecal eDecal) {
        TextureRegion txReg = eDecal.getTextureRegion();
        eDecal.decal.setDimensions(txReg.getRegionWidth() / PPU, txReg.getRegionHeight() / PPU);
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
        // TODO
    }

    public void update(float delta) {
        // TODO
    }

    public void setPositionWp(float wx, float wy, float wz) {
        positionWp.set(wx, wy, wz);

        for (EntityDecal eDecal : decals) {
            eDecal.decal.setPosition(
                    wx + eDecal.offsetWx + dimensions.x / 2.0f,
                    wy + eDecal.offsetWy,
                    wz + eDecal.offsetWz + dimensions.y / 2.0f);
        }
    }

    public void addToPosition(float wx, float wy, float wz) {
        setPositionWp(getWx() + wx, getWy() + wy, getWz() + wz);
    }

    public void setPositionWp(Vector3 positionWp) {
        setPositionWp(positionWp.x, positionWp.y, positionWp.z);
    }

    public void translate(Vector3 translation) {
        setPositionWp(positionWp.x + translation.x, positionWp.y + translation.y, positionWp.z + translation.z);
    }

    public float getWx() {
        return positionWp.x;
    }

    public float getWy() {
        return positionWp.y;
    }

    public float getWz() {
        return positionWp.z;
    }

    public Vector3 getPositionWpCopy() {
        return positionWp.cpy();
    }
}
