package vc.andro.poketest.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class HackFont {

    public static final String FONT_HACK_8_PT_FNT = "font-hack-8pt.fnt";
    public static final String FONT_HACK_8_PT_PNG = "font-hack-8pt.png";
    private final Texture fontTexture;
    private final BitmapFont bitmapFont;

    public HackFont(Texture fontTexture, BitmapFont bitmapFont) {
        this.fontTexture = fontTexture;
        this.bitmapFont = bitmapFont;
    }

    public void dispose() {
        bitmapFont.dispose();
        fontTexture.dispose();
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public static HackFont load() {
        FileHandle fontPng = Gdx.files.internal(FONT_HACK_8_PT_PNG);
        if (fontPng == null) {
            throw new RuntimeException("Failed to load " + FONT_HACK_8_PT_PNG);
        }
        FileHandle fontFnt = Gdx.files.internal(FONT_HACK_8_PT_FNT);
        if (fontFnt == null) {
            throw new RuntimeException("Failed to load " + FONT_HACK_8_PT_FNT);
        }
        var texture = new Texture(fontPng);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        var bitmapFont = new BitmapFont(fontFnt, new TextureRegion(texture), false);
        return new HackFont(texture, bitmapFont);
    }
}
