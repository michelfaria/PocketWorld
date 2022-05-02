package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;

public class Player extends Entity {

    public Player() {
        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.entityAtlas);
        TextureRegion playerLookDown = AtlasUtil.findRegion(atlas, "entity/player/walk-down", 1);
        EntityDecal decal = new EntityDecal(Decal.newDecal(playerLookDown));
        addDecal(decal);
    }
}
