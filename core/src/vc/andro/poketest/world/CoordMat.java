package vc.andro.poketest.world;

import com.badlogic.gdx.utils.IntMap;
import org.jetbrains.annotations.Nullable;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CoordMat<T> {

    public final IntMap<IntMap<T>> map; // (x -> y -> value)

    public CoordMat() {
        map = new IntMap<>();
    }

    /**
     * Gets an entry at (x, y) or null
     */
    public @Nullable
    T get(int x, int y) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            return null;
        }
        return ym.get(y);
    }

    /**
     * Sets an entry at (x, y). Returns a T if it replaced something.
     */
    public @Nullable
    T set(int x, int y, T val) {
        IntMap<T> ym = map.get(x);
        if (ym == null) {
            ym = new IntMap<>();
            map.put(x, ym);
        }
        return ym.put(y, val);
    }

    public Stream<T> values() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(map.values(), Spliterator.ORDERED), false)
                .map(IntMap::values)
                .flatMap(it -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false));
    }
}
