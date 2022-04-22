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
import vc.andro.poketest.PocketCamera;
import vc.andro.poketest.world.World;
import vc.andro.poketest.world.WorldRenderingStrategy;

public class MainRenderSystem implements Disposable {

    private final World world;

    private final PocketCamera cam;
    private final Environment env;
    private final ModelBatch modelBatch;
    private final MyCameraGroupStrategy cameraGroupStrategy;
    private final DecalBatch decalBatch;

    private final WorldRenderingStrategy worldRenderingStrategy;

    public MainRenderSystem(World world, PocketCamera cam) {
        this.world = world;
        this.cam = cam;
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
        env.add(new DirectionalLight().set(1.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f));
        cameraGroupStrategy = new MyCameraGroupStrategy(cam.getUnderlying());
        decalBatch = new DecalBatch(cameraGroupStrategy);
        modelBatch = new ModelBatch();
        DefaultShader.defaultCullFace = GL20.GL_FRONT;
        worldRenderingStrategy = new WorldRenderingStrategy(cam, world);

        {
            int surfaceWy = world.getSurfaceVoxelWy_WP(0, 0);
            if (surfaceWy != -1) {
                cam.getPosition().set(0, surfaceWy + 10, 0);
            }
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0.08235294f, 0.5411765f, 0.7411765f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        modelBatch.begin(cam.getUnderlying());
        modelBatch.render(worldRenderingStrategy, env);
        modelBatch.end();

        worldRenderingStrategy.updateViewpoint();
        worldRenderingStrategy.renderEntities(decalBatch);

        decalBatch.flush();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        cameraGroupStrategy.dispose();
        decalBatch.dispose();
    }
}
