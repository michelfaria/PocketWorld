package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
                    World.WxCx((int) (pokecam.getPosition().x / TILE_SIZE)),
                    World.WzCz((int) (pokecam.getPosition().y / TILE_SIZE)));
        }

        renderChunkBorders();
        renderTileYs();
    }

    private void renderTileYs() {
        spriteBatch.setColor(Color.BLUE);
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();

        int curTileWorldX = (int)pokecam.getPosition().x / TILE_SIZE;
        int curTileWorldZ = (int)pokecam.getPosition().y / TILE_SIZE;

        for (int wx = curTileWorldX - 10; wx < curTileWorldX + 10; wx++) {
            for (int wz = curTileWorldZ - 10; wz < curTileWorldZ + 10; wz++) {
                BasicTile surfaceTile = world.getSurfaceTile(wx, wz);
                if (surfaceTile == null) {
                    continue;
                }
                Vector3 renderPos = pokecam.project(new Vector3(wx*TILE_SIZE+ 8, wz * TILE_SIZE + 8, 0));
                bitmapFont.draw(spriteBatch, "" + surfaceTile.y, renderPos.x, renderPos.y);
            }
        }
        spriteBatch.end();
        spriteBatch.setColor(Color.WHITE);
    }

    private void renderChunkBorders() {
        shapeRenderer.setProjectionMatrix(pokecam.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        int curChunkX = World.WxCx((int) (pokecam.getPosition().x / TILE_SIZE));
        int curChunkZ = World.WzCz((int) (pokecam.getPosition().y / TILE_SIZE));

        for (int cx = curChunkX - 5; cx < curChunkX + 5; cx++) {
            for (int cz = curChunkZ - 5; cz < curChunkZ + 5; cz++) {
                int renderX = World.CxWx(cx) * TILE_SIZE;
                int renderY = World.CzWz(cz) * TILE_SIZE;
                int renderWidth = CHUNK_SIZE * TILE_SIZE;
                int renderHeight = CHUNK_SIZE * TILE_SIZE;
                shapeRenderer.rect(renderX, renderY, renderWidth, renderHeight);
            }
        }

        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();

        for (int cx = curChunkX - 5; cx < curChunkX + 5; cx++) {
            for (int cz = curChunkZ - 5; cz < curChunkZ + 5; cz++) {
                final int renderX = World.CxWx(cx) * TILE_SIZE;
                final int renderY = World.CzWz(cz) * TILE_SIZE;
                bitmapFont.draw(spriteBatch, "" + cx + ", " + cz, renderX + 5, renderY - 5);
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

        Rectangle visibleArea = pokecam.getVisibleArea();

        for (int worldX = (int) visibleArea.x / TILE_SIZE - CAMERA_CULL_LEEWAY;
             worldX < visibleArea.x / TILE_SIZE + visibleArea.width / TILE_SIZE;
             worldX++
        ) {
            for (int worldZ = (int) visibleArea.y / TILE_SIZE - CAMERA_CULL_LEEWAY;
                 worldZ < visibleArea.y / TILE_SIZE + visibleArea.height / TILE_SIZE;
                 worldZ++
            ) {
                dbgInfo_iterations++;
                BasicTile surfaceTile = world.getSurfaceTile_G(worldX, worldZ);
                if (surfaceTile == null) {
                    continue;
                }
                BasicTile tile = world.getTileAt_G_WP(
                        worldX,
                        surfaceTile.y,
                        worldZ
                );
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
