package dev.inteleonyx.armandillo.core.impl;

import dev.inteleonyx.armandillo.ArmandilloMod;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.core.lang.functions.ArmandilloEventFunction;
import dev.inteleonyx.armandillo.core.lang.functions.ArmandilloLoadClassFunction;
import dev.inteleonyx.armandillo.core.lang.functions.ArmandilloModuleFunction;
import dev.inteleonyx.armandillo.core.registry.EventRegistry;
import dev.inteleonyx.armandillo.core.registry.ModuleRegistry;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import dev.inteleonyx.armandillo.luaj.ILuaEnvironment;
import dev.inteleonyx.armandillo.environment.impl.LuaJEnvironment;
import dev.inteleonyx.armandillo.core.IArmandilloLoader;
import dev.inteleonyx.armandillo.utils.ScriptFinder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */
public class ArmandilloLoader implements IArmandilloLoader {

    private final ILuaEnvironment luaEnvironment;

    public ArmandilloLoader() {
        this.luaEnvironment = new LuaJEnvironment();
    }

    @Override
    public void initialize() {
        LuaValue armandilloGlobal = LuaValue.tableOf();
        armandilloGlobal.set("module", new ArmandilloModuleFunction());
        armandilloGlobal.set("load_class", new ArmandilloLoadClassFunction());
        armandilloGlobal.set("event", new ArmandilloEventFunction());

        luaEnvironment.registerGlobal("Armandillo", armandilloGlobal);

        EventRegistry.getInstance().register(getEnvironment().getGlobals());
        ModuleRegistry.getInstance().register(getEnvironment().getGlobals());

        List<String> scriptPaths = ScriptFinder.find(ArmandilloMod.ARMANDILLO_ROOT_PATH);

        for (String path : scriptPaths) {
            try {
                String luaCode = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
                luaEnvironment.runScript(luaCode);
            } catch (Exception e) {
                System.err.println("[Armandillo] Failed to load script from path: " + path);
                e.printStackTrace();
            }
        }

        System.out.println("[Armandillo] Loader initialized.");
    }

    @Override
    public ILuaEnvironment getEnvironment() {
        return this.luaEnvironment;
    }

    @Override
    public void reloadScripts() {
        EventRegistry eventRegistry = EventRegistry.getInstance();
        ModuleRegistry moduleRegistry = ModuleRegistry.getInstance();

        eventRegistry.unregister();
        //moduleRegistry.register(luaEnvironment.getGlobals());

        eventRegistry.register(luaEnvironment.getGlobals());
        //moduleRegistry.unre

        List<String> scriptPaths = ScriptFinder.find(ArmandilloMod.ARMANDILLO_ROOT_PATH);

        for (String path : scriptPaths) {
            try {
                String luaCode = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
                luaEnvironment.runScript(luaCode);
            } catch (Exception e) {
                System.err.println("[Armandillo] Failed to load script from path: " + path);
                e.printStackTrace();
            }
        }
    }
}