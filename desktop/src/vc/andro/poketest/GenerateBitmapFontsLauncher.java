package vc.andro.poketest;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import vc.andro.poketest.tools.GenerateBitmapFonts;

public class GenerateBitmapFontsLauncher {
    public static void main(String[] args) {
        new Lwjgl3Application(new GenerateBitmapFonts());
    }
}
