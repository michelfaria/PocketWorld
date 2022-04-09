package vc.andro.poketest.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import vc.andro.poketest.PokeTest;

import java.util.Objects;

public class TreeEntity extends Entity {
    public TreeEntity() {
        super("tree",
                Objects.requireNonNull(PokeTest.getTextureAtlas().findRegion("tree")));
    }
}
