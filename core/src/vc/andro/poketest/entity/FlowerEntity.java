package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PocketWorld.PPU;

public class FlowerEntity extends Entity {
    public FlowerEntity() {
        super();

        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.entityAtlas);
        {
            var mainDecal = new EntityDecal(Decal.newDecal(
                    AtlasUtil.findRegion(atlas, "entity/flower"),
                    true
            ));
            mainDecal.offsetWy = mainDecal.getTextureRegion().getRegionHeight() / PPU / 2f;
            addDecal(mainDecal);
        }
        {
            var mainDecal90 = new EntityDecal(Decal.newDecal(
                    AtlasUtil.findRegion(atlas, "entity/flower"),
                    true
            ));
            mainDecal90.offsetWy = mainDecal90.getTextureRegion().getRegionHeight() / PPU / 2f;
            mainDecal90.yaw = 90.0f;
            addDecal(mainDecal90);
        }
    }
}
