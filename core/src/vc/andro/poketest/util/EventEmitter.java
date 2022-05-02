package vc.andro.poketest.util;

import com.badlogic.gdx.utils.Array;

public class EventEmitter<T> {
    private final Array<EventListener<T>> listeners        = new Array<>(EventListener.class);
    private final Array<EventListener<T>> oneTimeListeners = new Array<>(EventListener.class);

    public synchronized void addListener(EventListener<T> eventListener) {
        listeners.add(eventListener);
    }

    public synchronized void addOneTimeListener(EventListener<T> eventListener) {
        oneTimeListeners.add(eventListener);
    }

    public synchronized void emit(T event) {
        for (EventListener<T> listener : listeners) {
            listener.handle(event);
        }
        for (Array.ArrayIterator<EventListener<T>> iterator = oneTimeListeners.iterator(); iterator.hasNext(); ) {
            EventListener<T> listener = iterator.next();
            if (listener.handle(event)) {
                iterator.remove();
            }
        }
    }

    public synchronized boolean remove(EventListener<T> listener) {
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
