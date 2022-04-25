package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import vc.andro.poketest.graphics.DebugRenderSystem;
import vc.andro.poketest.graphics.MainRenderSystem;
import vc.andro.poketest.registry.GeneralSettingsRegistry;
import vc.andro.poketest.world.Chunk;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldCreationParams;
import vc.andro.poketest.world.generation.WorldGenerator;

public class PlayScreen implements Screen {

    private final PocketCamera cam;
    private final World world;
    private final MainRenderSystem mainRenderSystem;
    private final DebugRenderSystem debugRenderSystem;

    private float timeSinceLastTick;

    public PlayScreen(WorldCreationParams worldCreationParams) {
        world = new WorldGenerator(worldCreationParams).getWorld();
        cam = new PocketCamera(world);
        mainRenderSystem = new MainRenderSystem(world, cam);
        debugRenderSystem = new DebugRenderSystem(world, cam);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);

        mainRenderSystem.render();
        debugRenderSystem.render();
    }

    private void update(float delta) {
        if (timeSinceLastTick >= 1.0f / PocketWorld.TICKS_PER_SECOND) {
            world.tick();
            timeSinceLastTick = Math.max(timeSinceLastTick - (1.0f / PocketWorld.TICKS_PER_SECOND), 0.0f);
        }
        timeSinceLastTick += delta;
        world.update(delta);
        mainRenderSystem.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            world.getChunks().values().forEach(Chunk::forceRerender);
        }

        if (GeneralSettingsRegistry.debugChunkGenerateOnKeyPress && Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            world.generateChunkAtViewpoint();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Chunk c = world.getChunkAtViewpoint();
            if (c != null) {
                c.slopifyVoxels(true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        cam.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void dispose() {
        mainRenderSystem.dispose();
        debugRenderSystem.dispose();
    }
}
