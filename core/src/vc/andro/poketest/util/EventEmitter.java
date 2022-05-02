package vc.andro.poketest.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class EventEmitter<T>  implements Pool.Poolable {
    private final Array<EventListener<T>> listeners;

    public EventEmitter() {
        listeners = new Array<>();
    }

    @Override
    public void reset() {
        listeners.clear();
    }

    public void addListener(EventListener<T> eventListener) {
        listeners.add(eventListener);
    }

    public void emit(T event) {
        for (EventListener<T> listener : listeners) {
            listener.handle(event);
        }
    }

    public boolean remove(EventListener<T> listener) {
        boolean anyRemoved = false;
        for (Array.ArrayIterator<EventListener<T>> iterator = listeners.iterator(); iterator.hasNext(); ) {
            EventListener<T> oListener = iterator.next();
            if (oListener.equals(listener)) {
                iterator.remove();
                anyRemoved = true;
            }
        }
        return anyRemoved;
    }
}
