package vc.andro.poketest.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.util.AtlasUtil;

import static vc.andro.poketest.PocketWorld.TILE_SIZE;

public class TallGrassEntity extends Entity {
    public TallGrassEntity() {
        super(new Vector2(1, 1));

        int variation = MathUtils.random(0, 1);

        TextureAtlas atlas = PocketWorld.assetManager.get(Assets.entityAtlas);
        for (int i = 0; i <= 1; i++) {
            var decal = new EntityDecal(Decal.newDecal(
                    AtlasUtil.findRegion(atlas, "entity/tall_grass/tgrass%d%d".formatted(variation, i)),
                    true
            ));
            decal.pitch = 90.0f;
            decal.offsetWy = i * 2.0f / TILE_SIZE;
            addDecal(decal);
        }
    }
}
