package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.entity.Player;
import vc.andro.poketest.tile.BasicTile;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldBaseCreator;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.WorldGenerator;

import java.util.Random;

import static vc.andro.poketest.PokeTest.*;

public class PlayScreen implements Screen {

    public static final int TICKS_PER_SECOND = 16;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final BitmapFont bitmapFont;
    private final World world;
    private final SpriteBatch spriteBatch;
    private final Player player;

    private boolean freeCam = false;
    private float timeSinceLastTick = 0;
    private int dbgInfo_tilesDrawn = 0;
    private int dbgInfo_iterations = 0;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        world = new WorldGenerator(new WorldBaseCreator(worldCreationParams).createBase()).createWorld();
        bitmapFont = assetManager.get(Assets.hackFont8pt);
        spriteBatch = new SpriteBatch();

        player = new Player();
        world.addEntity(player);
        // find random tile player can stand on
        var random = new Random();
        while (true) {
            int x = random.nextInt(world.getWidth());
            int y = random.nextInt(world.getHeight());
            BasicTile tile = world.getTile(x, y);
            if (tile.canPlayerWalkOnIt()) {
                player.setPosition(x, y);
                break;
            }
        }
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
        renderInfo();
    }

    private void renderInfo() {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch,
                "fps: " + Gdx.graphics.getFramesPerSecond()
                        + ", tiles drawn: " + dbgInfo_tilesDrawn
                        + ", iters: " + dbgInfo_iterations
                , 0, 28);
        spriteBatch.end();
    }

    private void renderTiles() {
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (var y = 0; y < world.getHeight(); y++) {
            for (var x = 0; x < world.getWidth(); x++) {
                dbgInfo_iterations++;
                int nx = x * TILE_SIZE;
                int ny = y * TILE_SIZE;

                // cull rendering outside of the frustrum
                if (isPosOutsideOfCameraView(nx, ny)) {
                    continue;
                }

                BasicTile tile = world.getTile(x, y);
                tile.draw(spriteBatch);
                dbgInfo_tilesDrawn++;
            }
        }
        spriteBatch.end();
    }

    private void renderEntities() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Entity entity : world.getEntities()) {
            float nx = entity.x * TILE_SIZE;
            float ny = entity.y * TILE_SIZE;
            if (isPosOutsideOfCameraView(nx, ny)) {
                continue;
            }
            entity.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    private boolean isPosOutsideOfCameraView(float x, float y) {
        return !camera.frustum.boundsInFrustum(x, y, 0, TILE_SIZE, TILE_SIZE, 0);
    }

    private void update(float delta) {
        if (timeSinceLastTick >= 1.0f / TICKS_PER_SECOND) {
            world.tick();
            timeSinceLastTick = Math.max(timeSinceLastTick - (1.0f / TICKS_PER_SECOND), 0.0f);
        }
        timeSinceLastTick += delta;
        world.update(delta);
        updateCameraPosition();
        camera.update();
        viewport.apply();
    }

    private void updateCameraPosition() {
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
        } else {
            camera.position.set(
                    player.getX() * TILE_SIZE + TILE_SIZE / 2f,
                    player.getY() * TILE_SIZE + TILE_SIZE / 2f, 1);
            camera.zoom = 0.33f;
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
        spriteBatch.dispose();
    }
}
