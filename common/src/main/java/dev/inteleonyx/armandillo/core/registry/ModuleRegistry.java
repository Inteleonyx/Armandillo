package dev.inteleonyx.armandillo.core.registry;

import dev.inteleonyx.armandillo.api.luaj.Globals;
import dev.inteleonyx.armandillo.core.objects.ArmandilloModule;
import dev.inteleonyx.armandillo.core.lang.functions.ArmandilloModuleFunction;
import dev.inteleonyx.armandillo.registry.ArmandilloRegistries;

import java.util.Map;
import java.util.Optional;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class ModuleRegistry {
    private static final ModuleRegistry INSTANCE = new ModuleRegistry();
    Map<String, ArmandilloModule> modules = ArmandilloRegistries.MODULES.getAll();

    private ModuleRegistry() {}

    public static ModuleRegistry getInstance() {
        return INSTANCE;
    }

    public void register(Globals globals) {
        for (Map.Entry<String, ArmandilloModule> entry : modules.entrySet()) {
            String moduleName = entry.getKey();
            ArmandilloModule module = entry.getValue();

            module.init();

            globals.set(moduleName, module.getModuleEngine());
        }
    }

    public Optional<ArmandilloModule> getModule(String moduleName) {
        return Optional.ofNullable(modules.get(moduleName));
    }
}
