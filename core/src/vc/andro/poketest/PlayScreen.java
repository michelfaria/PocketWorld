package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicTile;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.WorldGenerator;

import static vc.andro.poketest.PokeTest.TILE_SIZE;
import static vc.andro.poketest.PokeTest.assetManager;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class PlayScreen implements Screen {

    private static final int TICKS_PER_SECOND = 16;
    private static final int VIEWPORT_HEIGHT = 600;
    private static final int VIEWPORT_WIDTH = 800;
    public static final int CAMERA_CULL_LEEWAY = 50;

    private final Pokecam pokecam;
    private final BitmapFont bitmapFont;
    private final World world;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    //   private final Player player;

    private boolean freeCam = false;
    private float timeSinceLastTick = 0;
    private int dbgInfo_tilesDrawn = 0;
    private int dbgInfo_iterations = 0;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        pokecam = new Pokecam(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        world = new WorldGenerator(worldCreationParams).createWorld();
        bitmapFont = assetManager.get(Assets.hackFont8pt);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

//        player = new Player();
//        world.addEntity(player);
        // find random tile player can stand on
//        var random = new Random();
//        while (true) {
//            int x = random.nextInt(1000);
//            int y = random.nextInt(1000);
//            BasicTile tile = world.getTileAt(x, y);
//            if (tile !=null) {
//            if (tile.canPlayerWalkOnIt()) {
//                player.setPosition(x, y);
//                break;
//            }}
//        }
        //pokecam.followEntity = player;
        pokecam.freeCam = true;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            world.updateChunk(
                    World.worldXToChunkX((int) (pokecam.getPosition().x / TILE_SIZE)),
                    World.worldYToChunkY((int) (pokecam.getPosition().y / TILE_SIZE)));
        }

        renderChunkBorders();
    }

    private void renderChunkBorders() {
        shapeRenderer.setProjectionMatrix(pokecam.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        int curChunkX = World.worldXToChunkX((int) (pokecam.getPosition().x / TILE_SIZE));
        int curChunkY = World.worldYToChunkY((int) (pokecam.getPosition().y / TILE_SIZE));

        for (int cx = curChunkX - 5; cx < curChunkX + 5; cx++) {
            for (int cy = curChunkY - 5; cy < curChunkY + 5; cy++) {
                final int renderX = World.chunkXToWorldX(cx) * TILE_SIZE;
                final int renderY = World.chunkYToWorldY(cy) * TILE_SIZE;
                final int renderWidth = CHUNK_SIZE * TILE_SIZE;
                final int renderHeight = CHUNK_SIZE * TILE_SIZE;
                shapeRenderer.rect(renderX, renderY, renderWidth, renderHeight);
            }
        }

        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();

        for (int cx = curChunkX - 5; cx < curChunkX + 5; cx++) {
            for (int cy = curChunkY - 5; cy < curChunkY + 5; cy++) {
                final int renderX = World.chunkXToWorldX(cx) * TILE_SIZE;
                final int renderY = World.chunkYToWorldY(cy) * TILE_SIZE;
                bitmapFont.draw(spriteBatch, "" + cx + ", " + cy, renderX + 5, renderY - 5);
            }
        }

        spriteBatch.end();
    }

    private void renderInfo() {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch,
                "fps: " + Gdx.graphics.getFramesPerSecond()
                        + ", tiles drawn: " + dbgInfo_tilesDrawn
                        + ", iters: " + dbgInfo_iterations
                        + ", x: " + pokecam.getPosition().x / TILE_SIZE
                        + ", y: " + pokecam.getPosition().y / TILE_SIZE,
                0, 28);
        spriteBatch.end();
    }

    private void renderTiles() {
        pokecam.use();
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();

        final Rectangle visibleArea = pokecam.getVisibleArea();

        for (int worldX = (int) visibleArea.x / TILE_SIZE - CAMERA_CULL_LEEWAY;
             worldX < visibleArea.x / TILE_SIZE + visibleArea.width / TILE_SIZE;
             worldX++
        ) {
            for (int worldY = (int) visibleArea.y / TILE_SIZE - CAMERA_CULL_LEEWAY;
                 worldY < visibleArea.y / TILE_SIZE + visibleArea.height / TILE_SIZE;
                 worldY++
            ) {
                dbgInfo_iterations++;
                BasicTile tile = world.getTileOrGenerateAt(worldX, worldY);
                if (tile != null) {
                    tile.draw(spriteBatch);
                    dbgInfo_tilesDrawn++;
                }
            }
        }
        spriteBatch.end();
    }

    private void renderEntities() {
        pokecam.use();
        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();
        for (Entity entity : world.getEntities()) {
            float nx = entity.worldX * TILE_SIZE;
            float ny = entity.worldZ * TILE_SIZE;
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
