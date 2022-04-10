package vc.andro.poketest.tools;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.hiero.Hiero;

public class GenerateBitmapFonts extends ApplicationAdapter {
    @Override
    public void create() {
        var info = new BitmapFontWriter.FontInfo();
        info.padding = new BitmapFontWriter.Padding(1, 1, 1, 1);

        var param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 8;
        param.gamma = 2f;
        param.shadowOffsetY = 1;
        param.renderCount = 3;
        param.shadowColor = new Color(0, 0, 0, 0.45f);
        param.characters = Hiero.EXTENDED_CHARS;
        param.packer = new PixmapPacker(
                512,
                512,
                Pixmap.Format.RGBA8888,
                2,
                false,
                new PixmapPacker.SkylineStrategy()
        );

        var generator = new FreeTypeFontGenerator(Gdx.files.absolute("fonts/hack.ttf"));
        FreeTypeFontGenerator.FreeTypeBitmapFontData data = generator.generateData(param);

        BitmapFontWriter.writeFont(
                data,
                new String[]{"font.png"},
                Gdx.files.absolute("assets/font-hack-8pt.fnt"), info,
                512,
                512
        );

        BitmapFontWriter.writePixmaps(param.packer.getPages(), Gdx.files.absolute("assets"), "font-hack-8pt");
        System.exit(0);
    }
}
