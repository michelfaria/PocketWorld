package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.jetbrains.annotations.Nullable;
import vc.andro.poketest.entity.Entity;

import static vc.andro.poketest.PokeTest.TILE_SIZE;

public class Pokecam {

    public static final int CAM_SPEED = 30;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    public boolean freeCam;
    public @Nullable
    Entity followEntity = null;

    public Pokecam(float worldWidth, float worldHeight) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
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
                dx = -CAM_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                dx = CAM_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                dx = 0;
                dy = CAM_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                dx = 0;
                dy = -CAM_SPEED;
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
                    followEntity.getWorldX() * TILE_SIZE + TILE_SIZE / 2f,
                    followEntity.getWorldY() * TILE_SIZE + TILE_SIZE / 2f, 1);
            camera.zoom = 0.33f;
        }
    }

    public void resize(int width, int height) {
        viewport.setScreenSize(width, height);
    }

    public Rectangle getVisibleArea() {
        var rect = new Rectangle();
        rect.x = camera.position.x - camera.viewportWidth / 2f * camera.zoom;
        rect.y = camera.position.y - camera.viewportHeight / 2f * camera.zoom;
        rect.width = camera.viewportWidth * camera.zoom;
        rect.height = camera.viewportHeight * camera.zoom;
        return rect;
    }

    public Vector3 getPosition() {
        return camera.position;
    }
}
