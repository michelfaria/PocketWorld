package vc.andro.poketest.util;

import com.badlogic.gdx.utils.FloatArray;

public class VertexArray {

    public static final int VERTEX_SIZE = 8;

    private final FloatArray vertices;
    private int amount;

    public VertexArray() {
        vertices = new FloatArray();
    }

    public void addVertex6f(float x, float y, float z, float u, float v) {
        vertices.add(x, y, z, u);
        vertices.add(v);
        amount++;
    }

    public void addVertex8f(float x, float y, float z, float normalX, float normalY, float normalZ, float u, float v) {
        vertices.add(x, y, z, normalX);
        vertices.add(normalY, normalZ, u, v);
        amount++;
    }

    public float[] getItems() {
        return vertices.items;
    }

    public int getAmountVertices() {
        return amount;
    }

    public void clear() {
        vertices.clear();
    }
}
