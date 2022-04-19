package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.PocketCamera;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PocketWorld.TILE_SIZE;

public class TreeEntity extends Entity {

    public static final float COLLISION_WIDTH = 2f;
    public static final float COLLISION_HEIGHT = 2f;

    private final TextureRegion treeLeafBaseRegion;
    private final TextureRegion treeLeafTopRegion;
    private final TextureRegion treeTrunkRegion;
    private final Decal treeLeafBase;
    private final Decal treeLeafTop;
    private final Decal treeTrunk;

    public TreeEntity() {
        super("entity/tree/tree_main", new Vector2(COLLISION_WIDTH, COLLISION_HEIGHT));
        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.entityAtlas);
        treeLeafBaseRegion = AtlasUtil.findRegion(atlas, "entity/tree/tree_leaf_base");
        treeLeafBase = Decal.newDecal(treeLeafBaseRegion, true);
        treeLeafTopRegion = AtlasUtil.findRegion(atlas, "entity/tree/tree_leaf_top");
        treeLeafTop = Decal.newDecal(treeLeafTopRegion, true);
        treeTrunkRegion = AtlasUtil.findRegion(atlas, "entity/tree/tree_trunk");
        treeTrunk = Decal.newDecal(treeTrunkRegion, true);

        treeLeafBase.setDimensions(
                treeLeafBaseRegion.getRegionWidth() / (float) TILE_SIZE,
                treeLeafBaseRegion.getRegionHeight() / (float) TILE_SIZE
        );
        treeLeafTop.setDimensions(
                treeLeafTopRegion.getRegionWidth() / (float) TILE_SIZE,
                treeLeafTopRegion.getRegionHeight() / (float) TILE_SIZE
        );
        treeTrunk.setDimensions(
                treeTrunkRegion.getRegionWidth() / (float) TILE_SIZE,
                treeTrunkRegion.getRegionHeight() / (float) TILE_SIZE
        );
    }

    @Override
    public void draw(DecalBatch decalBatch, PocketCamera pocketCamera) {
        super.draw(decalBatch, pocketCamera);
        treeLeafBase.setRotation(0, 90, 0);
        treeLeafTop.setRotation(0, 90, 0);
        treeTrunk.setRotation(0, 90, 0);
        decalBatch.add(treeLeafBase);
        decalBatch.add(treeLeafTop);
        decalBatch.add(treeTrunk);
    }

    @Override
    public void setPosition(float wx, float y, float wz) {
        super.setPosition(wx, y, wz);
        treeLeafBase.setPosition(
                wx + treeLeafBaseRegion.getRegionWidth() / (float) TILE_SIZE / 2f,
                y + 26 / (float) TILE_SIZE,
                wz + treeLeafBaseRegion.getRegionHeight() / (float) TILE_SIZE);
        treeLeafTop.setPosition(wx + treeLeafTopRegion.getRegionWidth() / (float) TILE_SIZE / 2f,
                y + 53 / (float) TILE_SIZE,
                wz + treeLeafTopRegion.getRegionHeight() / (float) TILE_SIZE);
        treeTrunk.setPosition(
                wx + treeTrunkRegion.getRegionWidth() / (float) TILE_SIZE / 2f,
                y + 1 / (float) TILE_SIZE,
                wz + treeTrunkRegion.getRegionHeight() / (float) TILE_SIZE);
    }
}
