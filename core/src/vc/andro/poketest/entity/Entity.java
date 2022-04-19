package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.PocketCamera;
import vc.andro.poketest.graphics.Duocal;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PocketWorld.TILE_SIZE;

public class Entity {
    private float worldX;
    private float y;
    private float worldZ;

    protected Vector2 collisionDims;
    protected TextureRegion textureRegion;
    protected Duocal decal;

    public Entity(String spriteId) {
        this(spriteId, new Vector2(0, 0));
    }

    public Entity(String spriteId, Vector2 collisionDims) {
        textureRegion = AtlasUtil.findRegion(PocketWorld.assetManager.get(Assets.entityAtlas), spriteId);
        decal = new Duocal(textureRegion, true);
        decal.setDimensions(
                textureRegion.getRegionWidth() / (float) TILE_SIZE,
                textureRegion.getRegionHeight() / (float) TILE_SIZE
        );
        this.collisionDims = collisionDims;
    }

    public void draw(DecalBatch decalBatch, PocketCamera pocketCamera) {
        decal.setRotation(0, 0, 0);
        decal.addToBatch(decalBatch);
    }

    public void tick() {
    }

    public void update(float delta) {
    }

    public void setPosition(float worldX, float y, float worldZ) {
        this.worldX = worldX;
        this.y = y;
        this.worldZ = worldZ;

        decal.setPosition(
                worldX + (textureRegion.getRegionWidth() / (float) TILE_SIZE / 2f),
                y + (textureRegion.getRegionHeight() / (float) TILE_SIZE / 2f),
                worldZ + (textureRegion.getRegionHeight() / (float) TILE_SIZE / 2f)
        );
    }

    public float getWorldX() {
        return worldX;
    }

    public float getY() {
        return y;
    }

    public float getWorldZ() {
        return worldZ;
    }
}
