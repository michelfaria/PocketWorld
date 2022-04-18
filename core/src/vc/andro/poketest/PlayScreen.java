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
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import vc.andro.poketest.entity.Entity;
import vc.andro.poketest.graphics.MyCameraGroupStrategy;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.WorldGenerator;

import java.util.Stack;

import static vc.andro.poketest.PokeTest.TILE_SIZE;
import static vc.andro.poketest.PokeTest.assetManager;
import static vc.andro.poketest.world.Chunk.CHUNK_SIZE;

public class PlayScreen implements Screen {

    private static final int TICKS_PER_SECOND = 16;

    private final Pokecam pokecam;
    private final BitmapFont bitmapFont;
    private final World world;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final Environment environment;
    private final ModelBatch modelBatch;

    private final MyCameraGroupStrategy cameraGroupStrategy;
    private final DecalBatch decalBatch;

    private float timeSinceLastTick = 0;
    private int dbgInfo_tilesDrawn = 0;
    private int dbgInfo_iterations = 0;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        pokecam = new Pokecam();
        world = new WorldGenerator(worldCreationParams).createWorld();
        bitmapFont = assetManager.get(Assets.hackFont8pt);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        {
            modelBatch = new ModelBatch();
            DefaultShader.defaultCullFace = GL20.GL_FRONT;
        }
        {
            environment = new Environment();
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1.0f));
            environment.add(new DirectionalLight().set(1, 1, 1, 0, -1, 0));
        }
        {
            cameraGroupStrategy = new MyCameraGroupStrategy(pokecam.getUnderlying());
            decalBatch = new DecalBatch(cameraGroupStrategy);
        }
        {
            BasicVoxel t00 = world.getSurfaceTile(0, 0);
            if (t00 != null) {
                pokecam.getPosition().set(0, t00.y + 10, 0);
            }
        }
    }

    @Override
    public void show() {
        pokecam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.08235294f, 0.5411765f, 0.7411765f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        renderTiles3D();
        world.renderEntities(decalBatch, pokecam);
        decalBatch.flush();
        dbgInfo_renderInfo();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            world.updateChunk(
                    World.WxCx((int) (pokecam.getPosition().x / TILE_SIZE)),
                    World.WzCz((int) (pokecam.getPosition().y / TILE_SIZE)));
        }

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
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
                BasicVoxel surfaceTile = world.getSurfaceTile(wx, wz);
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
        bitmapFont.setColor(Color.PINK);
        bitmapFont.draw(spriteBatch,
                "fps: " + Gdx.graphics.getFramesPerSecond()
                        + ", tiles drawn: " + dbgInfo_tilesDrawn
                        + ", iters: " + dbgInfo_iterations
                        + ", camPos: (%.2f, %.2f, %.2f)".formatted(pokecam.getPosition().x, pokecam.getPosition().y, pokecam.getPosition().z)
                        + ", camDir: " + pokecam.getDirection().toString()
                        + ", chunksRendered: " + world.getDbgInfo_chunksRendered(),
                0, 40);
        bitmapFont.draw(spriteBatch,
                "entities rendered: " + world.getDbgInfo_entitiesRendered() +
                ", camUp: " + pokecam.getUp(),
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
                BasicVoxel surfaceTile = world.getSurfaceTile_G(worldX, worldZ);
                if (surfaceTile == null) {
                    continue;
                }
                Stack<BasicVoxel> drawStack = new Stack<>();
                drawStack.push(surfaceTile);
                if (surfaceTile.transparent) {
                    BasicVoxel under = world.getTileAt_G_WP(worldX, surfaceTile.y - 1, worldZ);
                    while (under != null) {
                        drawStack.push(under);
                        if (!under.transparent || under.y == 0) {
                            break;
                        }
                        under = world.getTileAt_G_WP(worldX, under.y - 1, worldZ);
                    }
                }
                while (!drawStack.empty()) {
                    BasicVoxel tile = drawStack.pop();
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
            float nx = entity.getWorldX() * TILE_SIZE;
            float ny = entity.getWorldZ() * TILE_SIZE;
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
        decalBatch.dispose();
        cameraGroupStrategy.dispose();
    }
}
