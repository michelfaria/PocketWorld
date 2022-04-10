package vc.andro.poketest.util;

import org.jetbrains.annotations.NotNull;

public interface EventListener<T> {
    void handle(@NotNull T t);
}
