package vc.andro.poketest.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.utils.Disposable;
import vc.andro.poketest.graphics.camera.PocketCamera;
import vc.andro.poketest.world.NoChunkException;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldRenderer;

public class MainRenderSystem implements Disposable {

    private final float clearColorR = 0.08235294f + 0.2f;
    private final float clearColorG = 0.5411765f + 0.2f;
    private final float clearColorB = 0.7411765f + 0.2f;
    private final float clearColorA = 1.0f;

    private final PocketCamera cam;
    private final Environment env;
    private final ModelBatch modelBatch;
    private final MyCameraGroupStrategy cameraGroupStrategy;
    private final DecalBatch decalBatch;

    private final WorldRenderer worldRenderer;

    public MainRenderSystem(World world, PocketCamera cam) {
        this.cam = cam;
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 0.8f));
        env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0.0f, -1.0f, 0.0f));
        cameraGroupStrategy = cam.createCameraGroupStrategy();
        decalBatch = new DecalBatch(cameraGroupStrategy);
        modelBatch = new ModelBatch(Gdx.files.internal("vertex.glsl"), Gdx.files.internal("fragment.glsl"));
        DefaultShader.defaultCullFace = GL20.GL_FRONT;
        worldRenderer = new WorldRenderer(cam, world);

        try {
            cam.getPosition().set(0, world.getSurfaceVoxelWy_WP(0, 0) + 10, 0);
        } catch (NoChunkException ignore) {
        }
    }

    public void render() {
        Gdx.gl.glClearColor(clearColorR, clearColorG, clearColorB, clearColorA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        cam.beginModelBatch(modelBatch);
        modelBatch.render(worldRenderer, env);
        modelBatch.end();

        worldRenderer.renderEntities(decalBatch);

        decalBatch.flush();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    public void update() {
        cam.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        cameraGroupStrategy.dispose();
        decalBatch.dispose();
    }
}
