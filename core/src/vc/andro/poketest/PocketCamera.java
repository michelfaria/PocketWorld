package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import org.jetbrains.annotations.Nullable;

import static vc.andro.poketest.PocketWorld.TILE_SIZE;

public class PocketCamera  {

    public static final float CAM_SPEED = 0.5f;

    private final PerspectiveCamera camera;

    public PocketCamera() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.5f;
        camera.far = 100f;
        camera.lookAt(0, -1, 0);
    }

    public Matrix4 getProjectionMatrix() {
        return camera.combined;
    }

    public Vector3 project(Vector3 worldCoords) {
        return camera.project(worldCoords);
    }

    public boolean isPosOutsideOfCameraView(float x, float z) {
        return !camera.frustum.boundsInFrustum(x, z, 0, TILE_SIZE, TILE_SIZE, 0);
    }

    public void update() {
        updatePosition();
        camera.update();
    }

    private void updatePosition() {
        /*
         * Update camera translation
         */
        float dx = 0;
        float dy = 0;
        float dz = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            dx = -CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            dx = CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            dz = CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            dz = -CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            dy = -CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            dy = CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            camera.rotate(0.2f, 1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) {
            camera.rotate(-0.2f, 1, 0, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            camera.rotate(-90f, 0, 1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            camera.rotate(90f, 0, 1, 0);
        }

        camera.translate(dx, dy, dz);
        camera.up.set(
                Math.round(camera.up.x),
                Math.round(camera.up.y),
                Math.round(camera.up.z)
        );
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
}
