package vc.andro.poketest.util;

import javax.annotation.CheckReturnValue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class CubicGroup<T> {

    public enum Face {
        TOP, BOTTOM, WEST, EAST, NORTH, SOUTH
    }

    public T top;
    public T bottom;
    public T west;
    public T east;
    public T north;
    public T south;

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

    @CheckReturnValue
    public <R> CubicGroup<R> map(BiFunction<T, Face, R> mapper) {
        return new CubicGroup<>(
                mapper.apply(top, Face.TOP),
                mapper.apply(bottom, Face.BOTTOM),
                mapper.apply(west, Face.WEST),
                mapper.apply(east, Face.EAST),
                mapper.apply(north, Face.NORTH),
                mapper.apply(south, Face.SOUTH)
        );
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
