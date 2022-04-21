package vc.andro.poketest.util;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import javax.annotation.CheckReturnValue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class CubicGroup<T> implements Pool.Poolable {

    @SuppressWarnings("rawtypes")
    public static final Pool<CubicGroup> pool = Pools.get(CubicGroup.class);

    public enum Face {
        TOP, BOTTOM, WEST, EAST, NORTH, SOUTH
    }

    public T top;
    public T bottom;
    public T west;
    public T east;
    public T north;
    public T south;

    private CubicGroup() {
    }

    public CubicGroup(T all) {
        this(all, all, all, all, all, all);
    }

    public CubicGroup(T top, T bottom, T west, T east, T north, T south) {
        this.top = top;
        this.bottom = bottom;
        this.west = west;
        this.east = east;
        this.north = north;
        this.south = south;
    }

    public void setup(T all) {
        setup(all, all, all, all, all, all);
    }

    public void setup(T top, T bottom, T west, T east, T north, T south) {
        this.top = top;
        this.bottom = bottom;
        this.west = west;
        this.east = east;
        this.north = north;
        this.south = south;
    }

    public void reset() {
        top = null;
        bottom = null;
        west = null;
        east = null;
        north = null;
        south = null;
    }

    @CheckReturnValue
    public <R> CubicGroup<R> mapPooled(BiFunction<T, Face, R> mapper) {
        //noinspection unchecked
        CubicGroup<R> g = (CubicGroup<R>) pool.obtain();
        g.setup(
                mapper.apply(top, Face.TOP),
                mapper.apply(bottom, Face.BOTTOM),
                mapper.apply(west, Face.WEST),
                mapper.apply(east, Face.EAST),
                mapper.apply(north, Face.NORTH),
                mapper.apply(south, Face.SOUTH)
        );
        return g;
    }

    @CheckReturnValue
    public boolean all(Function<T, Boolean> predicate) {
        return predicate.apply(top) && predicate.apply(bottom) && predicate.apply(west) && predicate.apply(east)
                && predicate.apply(north) && predicate.apply(south);
    }

    public void forEach(BiConsumer<T, Face> doFunc) {
        doFunc.accept(top, Face.TOP);
        doFunc.accept(bottom, Face.BOTTOM);
        doFunc.accept(west, Face.WEST);
        doFunc.accept(east, Face.EAST);
        doFunc.accept(north, Face.NORTH);
        doFunc.accept(south, Face.SOUTH);
    }

    @CheckReturnValue
    public Stream<T> flatStream() {
        return Stream.of(top, bottom, west, east, north, south);
    }

    public T getFace(Face which) {
        switch (which) {
            case TOP -> {
                return top;
            }
            case BOTTOM -> {
                return bottom;
            }
            case WEST -> {
                return west;
            }
            case EAST -> {
                return east;
            }
            case NORTH -> {
                return north;
            }
            case SOUTH -> {
                return south;
            }
            default -> throw new AssertionError();
        }
    }
}
