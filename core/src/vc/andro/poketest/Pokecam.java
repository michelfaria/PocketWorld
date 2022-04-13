package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Pokecam {

    public static final int CAM_SPEED = 5;
    private final PerspectiveCamera camera;

    public Pokecam(float worldWidth, float worldHeight) {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.5f;
        camera.far = 1000f;
        camera.position.set(-150, 10, 500);
        camera.direction.set(0, 0, -1);
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
        int dx = 0;
        int dy = 0;
        int dz = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            dx = -CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            dx = CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            dy = CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            dy = -CAM_SPEED;
        }
        /*
         * Update camera zoom
         */
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            dz = -CAM_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            dz = CAM_SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            camera.rotate(Vector3.X, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) {
            camera.rotate(Vector3.X, -1);
        }


        camera.translate(dx, dy, dz);
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public Vector3 getPosition() {
        return camera.position;
    }
}
