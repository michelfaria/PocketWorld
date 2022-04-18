package vc.andro.poketest.entity;

import com.badlogic.gdx.math.Vector2;

public class TreeEntity extends Entity {

    public static final float COLLISION_WIDTH = 2f;
    public static final float COLLISION_HEIGHT = 3f;

    public TreeEntity() {
        super("entity/tree", new Vector2(COLLISION_WIDTH, COLLISION_HEIGHT));
    }
}
