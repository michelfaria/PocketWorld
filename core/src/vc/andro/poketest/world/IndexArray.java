package vc.andro.poketest.world;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class IndexArray {

    private final ShortArray indices;
    private short j = 0;

    public IndexArray() {
        indices = new ShortArray();
    }

    public void addSquare() {
        indices.add(
                j,
                (short)(j + 1),
                (short)(j + 2),
                (short)(j + 2)
        );
        indices.add(
                (short) (j + 3),
                j
        );
        j += 4;
    }

    public short[] getItems() {
        return indices.items;
    }

    public void clear() {
        indices.clear();
        j = 0;
    }
}
