package vc.andro.poketest.entity;

public class TreeEntity extends Entity {

    public static final float COLLISION_WIDTH = 2f;
    public static final float COLLISION_HEIGHT = 3f;

    public TreeEntity() {
        super("entity/tree");
        collisionWidth = COLLISION_WIDTH;
        collisionHeight = COLLISION_HEIGHT;
    }
}
