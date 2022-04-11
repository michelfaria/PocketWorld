package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.entity.Player;
import vc.andro.poketest.tile.BasicTile;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldBaseCreator;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.WorldGenerator;

import java.util.Random;

import static vc.andro.poketest.PokeTest.TILE_SIZE;
import static vc.andro.poketest.PokeTest.assetManager;

public class PlayScreen implements Screen {

    public static final int TICKS_PER_SECOND = 16;

    private final Pokecam pokecam;
    private final BitmapFont bitmapFont;
    private final World world;
    private final SpriteBatch spriteBatch;
    private final Player player;

    private boolean freeCam = false;
    private float timeSinceLastTick = 0;
    private int dbgInfo_tilesDrawn = 0;
    private int dbgInfo_iterations = 0;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        pokecam = new Pokecam();
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
        pokecam.followEntity = player;
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
        pokecam.use();
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();
        for (var y = 0; y < world.getHeight(); y++) {
            for (var x = 0; x < world.getWidth(); x++) {
                dbgInfo_iterations++;
                int nx = x * TILE_SIZE;
                int ny = y * TILE_SIZE;

                // cull rendering outside of the frustrum
                if (pokecam.isPosOutsideOfCameraView(nx, ny)) {
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
        pokecam.use();
        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();
        for (Entity entity : world.getEntities()) {
            float nx = entity.x * TILE_SIZE;
            float ny = entity.y * TILE_SIZE;
            if (pokecam.isPosOutsideOfCameraView(nx, ny)) {
                continue;
            }
            entity.draw(spriteBatch);
        }
        spriteBatch.end();
    }


    private void update(float delta) {
        if (timeSinceLastTick >= 1.0f / TICKS_PER_SECOND) {
            world.tick();
            timeSinceLastTick = Math.max(timeSinceLastTick - (1.0f / TICKS_PER_SECOND), 0.0f);
        }
        timeSinceLastTick += delta;
        world.update(delta);
        pokecam.update();
    }


    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        pokecam.resize(width, height);
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
