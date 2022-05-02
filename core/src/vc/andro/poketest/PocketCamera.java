package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.world.World;

import static vc.andro.poketest.world.chunk.Chunk.CHUNK_DEPTH;
import static vc.andro.poketest.world.chunk.Chunk.CHUNK_SIZE;
import static vc.andro.poketest.world.World.CxWx;
import static vc.andro.poketest.world.World.CzWz;

public class PocketCamera extends InputAdapter {

    public static final float CAM_SPEED = 0.25f;

    private final PerspectiveCamera camera;
    private final World world;
    private final Vector3 tmp;

    private final float rotateSpeed = 1.0f;

    public PocketCamera(World world) {
        this.world = world;
        tmp = new Vector3();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.5f;
        camera.far = 3000f;
        camera.lookAt(0, 0, 0);
        camera.position.set(0, 128, 0);
    }

    public void update() {
        /*
         * Update camera translation
         */
        // Forward
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            tmp.set(camera.direction).nor().scl(1.0f, 0.0f, 1.0f).scl(CAM_SPEED);
            camera.position.add(tmp);
        }
        // Backward
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            tmp.set(camera.direction).nor().scl(1.0f, 0.0f, 1.0f).scl(-CAM_SPEED);
            camera.position.add(tmp);
        }
        // Left
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            tmp.set(camera.direction).crs(camera.up).nor().scl(-CAM_SPEED);
            camera.position.add(tmp);
        }
        // Right
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            tmp.set(camera.direction).crs(camera.up).nor().scl(CAM_SPEED);
            camera.position.add(tmp);
        }
        // Up
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            tmp.set(camera.up).nor().scl(CAM_SPEED);
            camera.position.add(tmp);
        }
        // Down
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            tmp.set(camera.up).nor().scl(-CAM_SPEED);
            camera.position.add(tmp);
        }
        // Yaw +
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.rotate(Vector3.Y, rotateSpeed);
        }
        // Yaw -
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.rotate(Vector3.Y, -rotateSpeed);
        }

        // Pitch +
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.direction.y += rotateSpeed / 10.0f;
            camera.direction.y = MathUtils.clamp(camera.direction.y, -1.0f, 1.0f);
        }

        // Pitch -
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.direction.y -= rotateSpeed / 10.0f;
            camera.direction.y = MathUtils.clamp(camera.direction.y, -1.0f, 1.0f);
        }


        camera.update();
        world.setViewpoint(camera.position.x, camera.position.y, camera.position.z);
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public Vector3 getPosition() {
        return camera.position;
    }

    public Vector3 getDirection() {
        return camera.direction;
    }

    public Camera getUnderlying() {
        return camera;
    }

    public Vector3 getUp() {
        return camera.up;
    }

    public boolean isChunkVisible(int cx, int cz) {
        return camera.frustum.boundsInFrustum(
                CxWx(cx) + CHUNK_SIZE / 2.0f,
                CHUNK_DEPTH / 2.0f,
                CzWz(cz) + CHUNK_SIZE / 2.0f,
                CHUNK_SIZE / 2.0f,
                CHUNK_DEPTH / 2.0f,
                CHUNK_SIZE / 2.0f
        );
    }

    public boolean isVisible(Entity entity) {
        return camera.frustum.boundsInFrustum(
                entity.getWx() + entity.dimensions.x / 2.0f,
                entity.getWy() + entity.dimensions.y / 2.0f,
                entity.getWz() + entity.dimensions.z / 2.0f,
                entity.dimensions.x / 2.0f,
                entity.dimensions.y / 2.0f,
                entity.dimensions.z / 2.0f
        );
    }

}
