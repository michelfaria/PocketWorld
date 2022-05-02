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
import vc.andro.poketest.world.World;

public class Player extends Entity {

    private static final float MOVE_SPEED = 0.1f;

    private final Vector3      forward = Vector3.Z.cpy().scl(-1.0f); // Face north
    private final Vector3      aux1    = new Vector3();
    private final Vector3      aux2    = new Vector3();
    private final Vector3      aux3    = new Vector3();
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
        aux3.set(camera.getDirection());
        aux3.set(Math.round(aux3.x), 0.0f, Math.round(aux3.z));

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            getPositionWp_out(aux2);
            aux1.set(aux3);
            aux1.scl(MOVE_SPEED);
            aux2.add(aux1);
            setPositionWp(aux2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            getPositionWp_out(aux2);
            aux1.set(aux3);
            aux1.scl(-MOVE_SPEED);
            aux2.add(aux1);
            setPositionWp(aux2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            getPositionWp_out(aux2);
            aux1.set(aux3);
            aux1.crs(camera.getUp());
            aux1.nor();
            aux1.scl(MOVE_SPEED);
            aux2.add(aux1);
            setPositionWp(aux2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            getPositionWp_out(aux2);
            aux1.set(aux3);
            aux1.crs(camera.getUp());
            aux1.nor();
            aux1.scl(-MOVE_SPEED);
            aux2.add(aux1);
            setPositionWp(aux2);
        }
        super.update(delta);
    }

    public Vector3 getForward() {
        return forward;
    }
}
