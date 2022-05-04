package vc.andro.poketest.world.chunk;

import com.badlogic.gdx.utils.IntMap;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CoordinateMatrix<T> {

    public final IntMap<IntMap<T>> map; // (x -> y -> value)

    public CoordinateMatrix() {
        map = new IntMap<>();
    }

    /**
     * Gets an entry at (x, y) or null
     */
    @Nullable
    public synchronized T get(int x, int y) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            return null;
        }
        return ym.get(y);
    }

    /**
     * Sets an entry at (x, y). Returns a T if it replaced something.
     */
    @SuppressWarnings("UnusedReturnValue")
    @Nullable
    public synchronized T set(int x, int y, T val) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            ym = new IntMap<>();
            map.put(x, ym);
        }
        return ym.put(y, val);
    }

    public synchronized void forEach(Consumer<T> fn) {
        for (IntMap<T> ym : map.values()) {
            for (T v : ym.values()) {
                fn.accept(v);
            }
        }
    }

    @Nullable
    public synchronized T remove(int x, int y) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            return null;
        }
        T t = ym.get(y);
        if (t == null) {
            return null;
        }
        ym.remove(y);
        if (ym.isEmpty()) {
            map.remove(x);
        }
        return t;
    }
}
