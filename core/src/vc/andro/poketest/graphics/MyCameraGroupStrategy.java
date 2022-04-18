package vc.andro.poketest.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;

public class MyCameraGroupStrategy implements GroupStrategy, Disposable {
    private static final int GROUP_OPAQUE = 0;
    private static final int GROUP_BLEND = 1;

    Pool<Array<Decal>> arrayPool = new Pool<Array<Decal>>(16) {
        @Override
        protected Array<Decal> newObject () {
            return new Array<>();
        }
    };
    Array<Array<Decal>> usedArrays = new Array<Array<Decal>>();
    ObjectMap<DecalMaterial, Array<Decal>> materialGroups = new ObjectMap<DecalMaterial, Array<Decal>>();

    Camera camera;
    ShaderProgram shader;
    private final Comparator<Decal> cameraSorter;

    public MyCameraGroupStrategy (Camera camera) {
        this.camera = camera;
        cameraSorter = (o1, o2) -> {
            float dist1 =MyCameraGroupStrategy.this.camera.position.dst(o1.getPosition());
            float dist2 = MyCameraGroupStrategy.this.camera.position.dst(o2.getPosition());
            return (int)Math.signum(dist2 - dist1);
        };
        createDefaultShader();
    }

    public MyCameraGroupStrategy (Camera camera, Comparator<Decal> sorter) {
        this.camera = camera;
        cameraSorter = sorter;
        createDefaultShader();
    }

    public void setCamera (Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera () {
        return camera;
    }

    @Override
    public int decideGroup (Decal decal) {
        return decal.getMaterial().isOpaque() ? GROUP_OPAQUE : GROUP_BLEND;
    }

    @Override
    public void beforeGroup (int group, Array<Decal> contents) {
        if (group == GROUP_BLEND) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            contents.sort(cameraSorter);
        } else {
            for (int i = 0, n = contents.size; i < n; i++) {
                Decal decal = contents.get(i);
                Array<Decal> materialGroup = materialGroups.get(decal.getMaterial());
                if (materialGroup == null) {
                    materialGroup = arrayPool.obtain();
                    materialGroup.clear();
                    usedArrays.add(materialGroup);
                    materialGroups.put(decal.getMaterial(), materialGroup);
                }
                materialGroup.add(decal);
            }

            contents.clear();
            for (Array<Decal> materialGroup : materialGroups.values()) {
                contents.addAll(materialGroup);
            }

            materialGroups.clear();
            arrayPool.freeAll(usedArrays);
            usedArrays.clear();
        }
    }

    @Override
    public void afterGroup (int group) {
        if (group == GROUP_BLEND) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void beforeGroups () {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        shader.bind();
        shader.setUniformMatrix("u_projectionViewMatrix", camera.combined);
        shader.setUniformi("u_texture", 0);
    }

    @Override
    public void afterGroups () {
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    private void createDefaultShader () {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projectionViewMatrix;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "precision mediump float;\n" //
                + "#endif\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  vec4 texel = v_color * texture2D(u_texture, v_texCoords);\n" +
                "  if (texel.a < 1.0) {\n" +
                "    discard;\n" +
                "  " +
                "}\n" +
                "  gl_FragColor = texel;\n" //
                + "}\n";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
    }

    @Override
    public ShaderProgram getGroupShader (int group) {
        return shader;
    }

    @Override
    public void dispose () {
        if (shader != null) shader.dispose();
    }
}
