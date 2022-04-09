package vc.andro.poketest;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import vc.andro.poketest.numbers.ViewRotation;

public class DrawingContext {
    public final SpriteBatch spriteBatch;
    public final ViewRotation viewRotation;

    public DrawingContext(SpriteBatch spriteBatch, ViewRotation viewRotation) {
        this.spriteBatch = spriteBatch;
        this.viewRotation = viewRotation;
    }

    public void dispose() {
        spriteBatch.dispose();
    }
}
