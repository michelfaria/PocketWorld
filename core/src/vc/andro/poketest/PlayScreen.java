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
import vc.andro.poketest.entity.Entity;
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
        decalBatch = new DecalBatch(new CameraGroupStrategy(pokecam.getUnderlying()));
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

        renderTiles3D();
        world.renderEntities(decalBatch, pokecam);
        decalBatch.flush();
        dbgInfo_renderInfo();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            world.updateChunk(
                    World.WxCx((int) (pokecam.getPosition().x / TILE_SIZE)),
                    World.WzCz((int) (pokecam.getPosition().y / TILE_SIZE)));
        }
    }

    private void renderTiles3D() {
        dbgInfo_tilesDrawn = 0;
        dbgInfo_iterations = 0;

        modelBatch.begin(pokecam.getUnderlying());
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
                        + ", camPos: (%.2f, %.2f, %.2f)".formatted(pokecam.getPosition().x, pokecam.getPosition().y, pokecam.getPosition().z)
                        + ", camRot: " + pokecam.getDirection().toString()
                        + ", chunksRendered: " + world.getDbgInfo_chunksRendered(),
                0, 40);
        bitmapFont.draw(spriteBatch,
                "entities rendered: " + world.getDbgInfo_entitiesRendered(),
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
    }
}
