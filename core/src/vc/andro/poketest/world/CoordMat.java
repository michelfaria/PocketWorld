package vc.andro.poketest.world;

import com.badlogic.gdx.utils.IntMap;
import org.jetbrains.annotations.Nullable;

public class CoordMat<T> {

    private final IntMap<IntMap<T>> map; // (x -> y -> value)

    public CoordMat() {
        map = new IntMap<>();
    }

    /**
     * Gets an entry at (x, y) or null
     */
    public @Nullable T get(int x, int y) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            return null;
        }
        return ym.get(y);
    }

    /**
     * Sets an entry at (x, y). Returns a T if it replaced something.
     */
    public @Nullable T set(int x, int y, T val) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            ym = new IntMap<>();
            map.put(x, ym);
        }
        return ym.put(y, val);
    }
}
