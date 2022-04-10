package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import vc.andro.poketest.font.HackFont;
import vc.andro.poketest.numbers.ViewRotation;
import vc.andro.poketest.world.Entity;
import vc.andro.poketest.world.Tile;
import vc.andro.poketest.world.World;
import vc.andro.poketest.worldgen.WorldBaseCreator;
import vc.andro.poketest.worldgen.WorldCreationParams;
import vc.andro.poketest.worldgen.WorldGenerator;

import javax.swing.text.View;

import static vc.andro.poketest.PokeTest.*;

public class PlayScreen implements Screen {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final HackFont hackFont;
    private final FPSLogger fpsLogger;
    private final World world;
    private final DrawingContext drawingContext;
    private final Timers timers;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        world = new WorldGenerator(new WorldBaseCreator(worldCreationParams).createBase()).createWorld();
        hackFont = HackFont.load();
        fpsLogger = new FPSLogger();
        drawingContext = new DrawingContext(new SpriteBatch(), new ViewRotation());
        timers = new Timers();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        renderTiles();
        renderEntities();
        //renderDebugNumbers();
        fpsLogger.log();
    }

    private void renderDebugNumbers() {
        final SpriteBatch spriteBatch = drawingContext.spriteBatch;

        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        spriteBatch.begin();
        for (var y = 0; y < world.getHeight(); y++) {
            for (var x = 0; x < world.getWidth(); x++) {
                int nx = x * TILE_SIZE;
                int ny = y * TILE_SIZE;

                // cull rendering outside of the frustrum
                if (isPosOutsideOfCameraView(nx, ny)) {
                    continue;
                }

                Tile tile = world.tiles[x][y];

                if (camera.zoom <= 0.5f) {
                    Vector3 tileScreenCoords = camera.project(new Vector3(nx, ny, 0));
                    hackFont.getBitmapFont().draw(spriteBatch, Float.toString(tile.getAltitude()),
                            tileScreenCoords.x, tileScreenCoords.y + 8);
                }
            }
        }
        spriteBatch.end();
    }

    private void renderTiles() {
        final SpriteBatch spriteBatch = drawingContext.spriteBatch;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (var y = 0; y < world.getHeight(); y++) {
            for (var x = 0; x < world.getWidth(); x++) {
                int nx = x * TILE_SIZE;
                int ny = y * TILE_SIZE;

                // cull rendering outside of the frustrum
                if (isPosOutsideOfCameraView(nx, ny)) {
                    continue;
                }

                Tile tile = world.tiles[x][y];
                tile.draw(drawingContext);
            }
        }
        spriteBatch.end();
    }

    private void renderEntities() {
        final SpriteBatch spriteBatch = drawingContext.spriteBatch;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Entity entity : world.entities) {
            float nx = entity.x * TILE_SIZE;
            float ny = entity.y * TILE_SIZE;
            if (isPosOutsideOfCameraView(nx, ny)) {
                continue;
            }
            entity.draw(drawingContext);
        }
        spriteBatch.end();
    }

    private boolean isPosOutsideOfCameraView(float x, float y) {
        return !camera.frustum.boundsInFrustum(x, y, 0, TILE_SIZE, TILE_SIZE, 0);
    }

    private void update(float delta) {
        updateCameraPosition();
        updateViewRotation();
        timers.tickTimers(delta);

        camera.update();
        viewport.apply();
    }

    private void updateCameraPosition() {
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
    }

    private void updateViewRotation() {
        final ViewRotation viewRotation = drawingContext.viewRotation;

        if (!timers.isTimerExpired(Timers.Id.CAMERA_ROTATION)) {
            return;
        }
        timers.resetTimer(Timers.Id.CAMERA_ROTATION);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            viewRotation.rotate90CCW();
            camera.rotate(-90f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) {
            viewRotation.rotate90CW();
            camera.rotate(90f);
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.setScreenSize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hackFont.dispose();
        drawingContext.dispose();
    }
}
