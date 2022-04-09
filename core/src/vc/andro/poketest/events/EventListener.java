package vc.andro.poketest.events;

import org.jetbrains.annotations.NotNull;

public interface EventListener<T> {
    void handle(@NotNull T t);
}
