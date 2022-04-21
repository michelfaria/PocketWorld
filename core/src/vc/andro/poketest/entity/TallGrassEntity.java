package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PocketWorld.PPU;

public class TallGrassEntity extends Entity {
    public TallGrassEntity() {
        int variation = 0;

        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.entityAtlas);
        for (int i = 0; i <= 1; i++) {
            var decal = new EntityDecal(Decal.newDecal(
                    AtlasUtil.findRegion(atlas, "entity/tall_grass/tgrass%d%d".formatted(variation, i)),
                    true
            ));
            decal.pitch = 90.0f;
            decal.offsetWy = i * 5.0f / PPU;
            addDecal(decal);
        }
    }
}
