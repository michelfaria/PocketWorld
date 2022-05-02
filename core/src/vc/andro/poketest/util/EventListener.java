package vc.andro.poketest.util;

import org.jetbrains.annotations.NotNull;

public interface EventListener<T> {
    boolean handle(@NotNull T t);
}
