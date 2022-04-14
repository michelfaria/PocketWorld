package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.tile.BasicTile;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.WorldGenerator;

import java.util.Stack;

import static vc.andro.poketest.PokeTest.TILE_SIZE;
import static vc.andro.poketest.PokeTest.assetManager;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class PlayScreen implements Screen {

    private static final int TICKS_PER_SECOND = 16;
    private static final int VIEWPORT_HEIGHT = 600;
    private static final int VIEWPORT_WIDTH = 800;

    private final Pokecam pokecam;
    private final BitmapFont bitmapFont;
    private final World world;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final Environment environment;
    private final ModelBatch modelBatch;

    private float timeSinceLastTick = 0;
    private int dbgInfo_tilesDrawn = 0;
    private int dbgInfo_iterations = 0;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        pokecam = new Pokecam(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        world = new WorldGenerator(worldCreationParams).createWorld();
        bitmapFont = assetManager.get(Assets.hackFont8pt);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        modelBatch = new ModelBatch();
        DefaultShader.defaultCullFace = GL20.GL_FRONT;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
        environment.add(new DirectionalLight().set(1, 1, 1, 0, -1, 0));
    }

    @Override
    public void show() {
        pokecam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        // renderTiles2D();
        //  renderEntities2D();
        renderTiles3D();
        dbgInfo_renderInfo();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            world.updateChunk(
                    World.WxCx((int) (pokecam.getPosition().x / TILE_SIZE)),
                    World.WzCz((int) (pokecam.getPosition().y / TILE_SIZE)));
        }

        //dbgInfo_renderChunkBorders();
        // dbgInfo_renderTileYs();
    }

    private void renderTiles3D() {
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        modelBatch.begin(pokecam.getUnderlying());
        modelBatch.render(world, environment);
        modelBatch.end();
    }

    private void dbgInfo_renderTileYs() {
        spriteBatch.setColor(Color.BLUE);
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();

        int curTileWorldX = (int) pokecam.getPosition().x / TILE_SIZE;
        int curTileWorldZ = (int) pokecam.getPosition().y / TILE_SIZE;

        for (int wx = curTileWorldX - 10; wx < curTileWorldX + 10; wx++) {
            for (int wz = curTileWorldZ - 10; wz < curTileWorldZ + 10; wz++) {
                BasicTile surfaceTile = world.getSurfaceTile(wx, wz);
                if (surfaceTile == null) {
                    continue;
                }
                Vector3 renderPos = pokecam.project(new Vector3(wx * TILE_SIZE + 8, wz * TILE_SIZE + 8, 0));
                bitmapFont.draw(spriteBatch, "" + surfaceTile.y, renderPos.x, renderPos.y);
            }
        }
        spriteBatch.end();
        spriteBatch.setColor(Color.WHITE);
    }

    private void dbgInfo_renderChunkBorders() {
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
                int renderX = World.CxWx(cx) * TILE_SIZE;
                int renderY = World.CzWz(cz) * TILE_SIZE;
                bitmapFont.draw(spriteBatch, "" + cx + ", " + cz, renderX + 5, renderY - 5);
            }
        }

        spriteBatch.end();
    }

    private void dbgInfo_renderInfo() {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch,
                "fps: " + Gdx.graphics.getFramesPerSecond()
                        + ", tiles drawn: " + dbgInfo_tilesDrawn
                        + ", iters: " + dbgInfo_iterations
                        + ", camPos: " + pokecam.getPosition().toString()
                        + ", camRot: " + pokecam.getDirection().toString()
                        + ", chunksRendered: " + world.getChunksRendered(),
                0, 28);
        spriteBatch.end();
    }

    private void renderTiles2D() {
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        spriteBatch.setProjectionMatrix(pokecam.getProjectionMatrix());
        spriteBatch.begin();

        int renderDistanceTiles = 8 * CHUNK_SIZE;
        float camWorldX = pokecam.getPosition().x / TILE_SIZE;
        float camWorldZ = pokecam.getPosition().y / TILE_SIZE;

        for (int worldX = (int) (camWorldX - renderDistanceTiles);
             worldX < camWorldX + renderDistanceTiles;
             worldX++
        ) {
            for (int worldZ = (int) (camWorldZ - renderDistanceTiles);
                 worldZ < camWorldZ + renderDistanceTiles;
                 worldZ++
            ) {
                dbgInfo_iterations++;
                BasicTile surfaceTile = world.getSurfaceTile_G(worldX, worldZ);
                if (surfaceTile == null) {
                    continue;
                }
                Stack<BasicTile> drawStack = new Stack<>();
                drawStack.push(surfaceTile);
                if (surfaceTile.transparent) {
                    BasicTile under = world.getTileAt_G_WP(worldX, surfaceTile.y - 1, worldZ);
                    while (under != null) {
                        drawStack.push(under);
                        if (!under.transparent || under.y == 0) {
                            break;
                        }
                        under = world.getTileAt_G_WP(worldX, under.y - 1, worldZ);
                    }
                }
                while (!drawStack.empty()) {
                    BasicTile tile = drawStack.pop();
                    tile.draw(spriteBatch);
                    dbgInfo_tilesDrawn++;
                }
            }
        }
        spriteBatch.end();
    }

    private void renderEntities2D() {
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
        world.setRenderPosition((int) pokecam.getPosition().x, (int) pokecam.getPosition().z);
    }


    private void clearScreen() {
        ScreenUtils.clear(0, 0, 0, 1f, true);
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
        modelBatch.dispose();
        shapeRenderer.dispose();
    }
}
