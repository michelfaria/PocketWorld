package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;

import static vc.andro.poketest.PokeTest.*;

public class Pokecam {

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    public boolean freeCam;
    public @Nullable
    Entity followEntity = null;

    public Pokecam() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
    }

    public Matrix4 getProjectionMatrix() {
        return camera.combined;
    }

    public boolean isPosOutsideOfCameraView(float x, float y) {
        return !camera.frustum.boundsInFrustum(x, y, 0, TILE_SIZE, TILE_SIZE, 0);
    }

    public void use() {
        viewport.apply();
    }

    public void update() {
        updatePosition();
        camera.update();
    }

    private void updatePosition() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            freeCam = !freeCam;
        }
        if (freeCam) {
            /*
             * Update camera translation
             */
            int dx = 0;
            int dy = 0;
            float dzoom = 0f;
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                dx = -100;
                dy = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                dx = 100;
                dy = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                dx = 0;
                dy = 100;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                dx = 0;
                dy = -100;
            }
            /*
             * Update camera zoom
             */
            if (Gdx.input.isKeyPressed(Input.Keys.U)) {
                dzoom = 0.1f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.O)) {
                dzoom = -0.1f;
            }

            camera.translate(dx, dy);

            // (do not zoom text camera)
            camera.zoom += dzoom;
            camera.zoom = Math.max(camera.zoom, 0.0f);
        } else if (followEntity != null) {
            camera.position.set(
                    followEntity.getX() * TILE_SIZE + TILE_SIZE / 2f,
                    followEntity.getY() * TILE_SIZE + TILE_SIZE / 2f, 1);
            camera.zoom = 0.33f;
        }
    }

    public void resize(int width, int height) {
        viewport.setScreenSize(width, height);
    }
}
