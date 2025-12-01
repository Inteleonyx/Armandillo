package dev.inteleonyx.armandillo.core.registry;

import dev.inteleonyx.armandillo.api.IArmandilloModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class ModuleRegistry {
    private static final ModuleRegistry INSTANCE = new ModuleRegistry();
    private final Map<String, IArmandilloModule> modules = new HashMap<>();

    private ModuleRegistry() {}

    public static ModuleRegistry getInstance() {
        return INSTANCE;
    }

    public void register(IArmandilloModule module) {
        if (modules.containsKey(module.getModuleId())) {
            throw new IllegalStateException("Duplicated module: " + module.getModuleId());
        }
        modules.put(module.getModuleId(), module);
        System.out.println("[Armandillo] Registered module: " + module.getModuleId());
    }

    public Optional<IArmandilloModule> getModule(String id) {
        return Optional.ofNullable(modules.get(id));
    }
}
