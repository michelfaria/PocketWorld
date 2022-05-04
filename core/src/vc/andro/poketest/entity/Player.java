package vc.andro.poketest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.graphics.camera.PocketCamera;
import vc.andro.poketest.util.AtlasUtil;
import vc.andro.poketest.util.VectorUtil;
import vc.andro.poketest.world.World;

public class Player extends Entity {

    private static final float MOVE_SPEED = 0.1f;

    private final Vector3      forward = Vector3.Z.cpy().scl(-1.0f); // Face north
    private final World        world;
    private final PocketCamera camera;

    public Player(World world, PocketCamera camera) {
        this.world = world;
        this.camera = camera;
        TextureAtlas atlas = PocketWorld.getAssetManager().get(Assets.entityAtlas);
        TextureRegion playerLookDown = AtlasUtil.findRegion(atlas, "entity/player/walk-down", 1);
        EntityDecal decal = new EntityDecal(Decal.newDecal(playerLookDown));
        addDecal(decal);
    }

    @Override
    public void update(float delta) {
        Vector3 horizontalMovementDirection =
                VectorUtil.round(
                        camera.getDirection()
                                .cpy()
                                .scl(1.0f, 0.0f, 1.0f));
        Vector3 deltaMovement = new Vector3();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            deltaMovement
                    .set(horizontalMovementDirection)
                    .scl(MOVE_SPEED);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            deltaMovement
                    .set(horizontalMovementDirection)
                    .scl(-MOVE_SPEED);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            deltaMovement
                    .set(horizontalMovementDirection)
                    .crs(camera.getUp())
                    .nor()
                    .scl(MOVE_SPEED);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            deltaMovement
                    .set(horizontalMovementDirection)
                    .crs(camera.getUp())
                    .nor()
                    .scl(-MOVE_SPEED);
        }

        translate(deltaMovement);

        super.update(delta);
    }

    public Vector3 getForwardCopy() {
        return forward.cpy();
    }
}
