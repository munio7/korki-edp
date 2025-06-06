package org.example.korkiedp.events;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    private static final Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>();

    public static <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public static <T> void publish(T event) {
        List<Consumer<?>> registered = listeners.get(event.getClass());
        if (registered != null) {
            for (Consumer<?> consumer : registered) {
                ((Consumer<T>) consumer).accept(event);
            }
        }
    }

    public static void clear() {
        listeners.clear();
    }
}
