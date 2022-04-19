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
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import vc.andro.poketest.graphics.MyCameraGroupStrategy;
import vc.andro.poketest.tile.BasicVoxel;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.generation.WorldGenerator;

import static vc.andro.poketest.PocketWorld.TILE_SIZE;
import static vc.andro.poketest.PocketWorld.assetManager;

public class PlayScreen implements Screen {

    private static final int TICKS_PER_SECOND = 16;

    private final PocketCamera pocketCamera;
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
        pocketCamera = new PocketCamera();
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
            cameraGroupStrategy = new MyCameraGroupStrategy(pocketCamera.getUnderlying());
            decalBatch = new DecalBatch(cameraGroupStrategy);
        }
        {
            BasicVoxel t00 = world.getSurfaceTile(0, 0);
            if (t00 != null) {
                pocketCamera.getPosition().set(0, t00.y + 10, 0);
            }
        }
    }

    @Override
    public void show() {
        pocketCamera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.08235294f, 0.5411765f, 0.7411765f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        renderTiles3D();
        world.renderEntities(decalBatch, pocketCamera);
        decalBatch.flush();
        dbgInfo_renderInfo();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            world.updateChunk(
                    World.WxCx((int) (pocketCamera.getPosition().x / TILE_SIZE)),
                    World.WzCz((int) (pocketCamera.getPosition().y / TILE_SIZE)));
        }

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    private void renderTiles3D() {
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        modelBatch.begin(pocketCamera.getUnderlying());
        modelBatch.render(world, environment);
        modelBatch.end();
    }


    private void dbgInfo_renderInfo() {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
        bitmapFont.setColor(Color.PINK);
        bitmapFont.draw(spriteBatch,
                "fps: " + Gdx.graphics.getFramesPerSecond()
                        + ", tiles drawn: " + dbgInfo_tilesDrawn
                        + ", iters: " + dbgInfo_iterations
                        + ", camPos: (%.2f, %.2f, %.2f)".formatted(pocketCamera.getPosition().x, pocketCamera.getPosition().y, pocketCamera.getPosition().z)
                        + ", camDir: " + pocketCamera.getDirection().toString()
                        + ", chunksRendered: " + world.getDbgChunksRendered(),
                0, 40);
        bitmapFont.draw(spriteBatch,
                "entities rendered: " + world.getDbgEntitiesRendered() +
                ", camUp: " + pocketCamera.getUp(),
                0, 28);
        spriteBatch.end();
    }


    private void update(float delta) {
        if (timeSinceLastTick >= 1.0f / TICKS_PER_SECOND) {
            world.tick();
            timeSinceLastTick = Math.max(timeSinceLastTick - (1.0f / TICKS_PER_SECOND), 0.0f);
        }
        timeSinceLastTick += delta;
        world.update(delta);
        pocketCamera.update();
        world.setRenderPosition((int) pocketCamera.getPosition().x, (int) pocketCamera.getPosition().z);
    }

    @Override
    public void resize(int width, int height) {
        pocketCamera.resize(width, height);
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
