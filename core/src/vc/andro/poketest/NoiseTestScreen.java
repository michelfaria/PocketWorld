package vc.andro.poketest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import vc.andro.poketest.world.WorldBase;
import vc.andro.poketest.world.WorldBaseCreator;
import vc.andro.poketest.world.WorldCreationParams;

@Deprecated
public class NoiseTestScreen implements Screen {

    private final WorldCreationParams creationParams;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Texture t;

    public NoiseTestScreen(WorldCreationParams creationParams) {
        this.creationParams = creationParams;
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(creationParams.getWidth(), creationParams.getHeight());
        camera.translate(creationParams.getWidth() / 2f, creationParams.getHeight() / 2f);

        var worldPlanner = new WorldBaseCreator(creationParams);
        WorldBase plannedWorld = worldPlanner.createBase();

        var pixmap = new Pixmap(creationParams.getWidth(), creationParams.getHeight(), Pixmap.Format.RGBA4444);

        for (int y = 0; y < creationParams.getHeight(); y++) {
            for (int x = 0; x < creationParams.getWidth(); x++) {
                float altitude = plannedWorld.getAltitudeMap()[x][y];
                int colorRgba8888;
                if (altitude > creationParams.getWaterLevel()) {
                    colorRgba8888 = Color.rgba8888(altitude, altitude, altitude, 1);
                } else {
                    colorRgba8888 = Color.rgba8888(0, 0, 1, 1);
                }
                pixmap.drawPixel(x, y, colorRgba8888);
            }
        }
        t = new Texture(pixmap);
    }

    @Override
    public void show() {
        Gdx.graphics.setResizable(false);
        Gdx.graphics.setWindowedMode(creationParams.getWidth(), creationParams.getHeight());
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
         * Render textures
         */
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(t, 0, 0);
        spriteBatch.end();
    }

    private void update() {
        /*
         * Update camera translation
         */
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            camera.translate(-10, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            camera.translate(10, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            camera.translate(0, 10);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            camera.translate(0, -10);
        }

        /*
         * Update camera zoom
         */
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            camera.zoom += 0.05f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            camera.zoom -= 0.05f;
        }
        camera.zoom = Math.max(camera.zoom, 0.0f);

        /*
         * Update components
         */
        camera.update();
        viewport.apply();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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

    }
}
