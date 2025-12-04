package dev.inteleonyx.armandillo.registry;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class ArmandilloRegistry<T> {
    private final Map<String, T> entries = new ConcurrentHashMap<>();

    public T register(String id, Supplier<T> supplier) {
        if (entries.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for id: " + id);
        }
        T instance = supplier.get();
        entries.put(id, instance);
        return instance;
    }

    public Optional<T> get(String id) {
        return Optional.ofNullable(entries.get(id));
    }

    public Map<String, T> getAll() {
        return Map.copyOf(entries);
    }
}
