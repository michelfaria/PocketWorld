package vc.andro.poketest.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class FreecamCameraStrategy implements CameraStrategy {

    private static final float CAM_SPEED    = 0.25f;
    private static final float ROTATE_SPEED = 1.0f;

    private final Vector3 tmp = new Vector3();

    @Override
    public void init(PocketCamera camera) {
        camera.lookAt(0, 0, 0);
        camera.getPosition().set(0, 128, 0);
    }

    @Override
    public void updateCamera(PocketCamera camera) {
        /*
         * Update camera translation
         */
        // Forward
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            tmp.set(camera.getDirection()).nor().scl(1.0f, 0.0f, 1.0f).scl(CAM_SPEED);
            camera.getPosition().add(tmp);
        }
        // Backward
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            tmp.set(camera.getDirection()).nor().scl(1.0f, 0.0f, 1.0f).scl(-CAM_SPEED);
            camera.getPosition().add(tmp);
        }
        // Left
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            tmp.set(camera.getDirection()).crs(camera.getUp()).nor().scl(-CAM_SPEED);
            camera.getPosition().add(tmp);
        }
        // Right
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            tmp.set(camera.getDirection()).crs(camera.getUp()).nor().scl(CAM_SPEED);
            camera.getPosition().add(tmp);
        }
        // Up
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            tmp.set(camera.getUp()).nor().scl(CAM_SPEED);
            camera.getPosition().add(tmp);
        }
        // Down
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            tmp.set(camera.getUp()).nor().scl(-CAM_SPEED);
            camera.getPosition().add(tmp);
        }
        // Yaw +
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.rotate(Vector3.Y, ROTATE_SPEED);
        }
        // Yaw -
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.rotate(Vector3.Y, -ROTATE_SPEED);
        }

        // Pitch +
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.getDirection().y += ROTATE_SPEED / 10.0f;
            camera.getDirection().y = MathUtils.clamp(camera.getDirection().y, -1.0f, 1.0f);
        }

        // Pitch -
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.getDirection().y -= ROTATE_SPEED / 10.0f;
            camera.getDirection().y = MathUtils.clamp(camera.getDirection().y, -1.0f, 1.0f);
        }

        camera.getWorld().setViewpoint(camera.getPosition());
    }
}
