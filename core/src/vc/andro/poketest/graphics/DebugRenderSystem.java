package vc.andro.poketest.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import vc.andro.poketest.Assets;
import vc.andro.poketest.PocketCamera;
import vc.andro.poketest.PocketWorld;
import vc.andro.poketest.registry.DebugInfoRegistry;
import vc.andro.poketest.world.World;


public class DebugRenderSystem implements Disposable {

    private final World world;
    private final PocketCamera cam;

    private final SpriteBatch spriteBatch;
    private final BitmapFont font;

    public DebugRenderSystem(World world, PocketCamera cam) {
        this.cam = cam;
        this.world = world;
        spriteBatch = new SpriteBatch();
        font = PocketWorld.assetManager.get(Assets.hackFont8pt);
    }

    public void render() {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
        font.setColor(Color.PINK);
        font.draw(spriteBatch,
                "fps: " + Gdx.graphics.getFramesPerSecond()
                        + ", camPos: (%.2f, %.2f, %.2f)".formatted(cam.getPosition().x, cam.getPosition().y, cam.getPosition().z)
                        + ", camDir: " + cam.getDirection().toString()
                        + ", chunksRendered: " + DebugInfoRegistry.chunksRendered,
                0, 40);
        font.draw(spriteBatch,
                "entities rendered: " + DebugInfoRegistry.entitiesRendered +
                        ", camUp: " + cam.getUp(),
                0, 28);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }
}
