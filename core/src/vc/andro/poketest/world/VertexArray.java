package vc.andro.poketest.world;

import com.badlogic.gdx.utils.FloatArray;

public class VertexArray {

    public static final int VERTEX_SIZE = 8;

    private final FloatArray vertices;
    private int amount;

    public VertexArray() {
        vertices = new FloatArray();
    }

    public void addVertex(float x,
                          float y,
                          float z,
                          float normalX,
                          float normalY,
                          float normalZ,
                          float u,
                          float v) {
        vertices.add(x, y, z, normalX);
        vertices.add(normalY, normalZ, u, v);
        amount += 1;
    }

    public float[] getVertices() {
        return vertices.items;
    }

    public int getAmountVertices() {
        return amount;
    }

    public void clear() {
        vertices.clear();
    }
}
