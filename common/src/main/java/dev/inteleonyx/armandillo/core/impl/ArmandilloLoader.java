package dev.inteleonyx.armandillo.core.impl;

import dev.inteleonyx.armandillo.ArmandilloMod;
import dev.inteleonyx.armandillo.lang.functions.ArmandilloLoadClass;
import dev.inteleonyx.armandillo.lang.functions.ArmandilloModuleFunction;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.lang.modules.recipes.RecipeModule;
import dev.inteleonyx.armandillo.core.registry.ModuleRegistry;
import dev.inteleonyx.armandillo.luaj.ILuaEnvironment;
import dev.inteleonyx.armandillo.env.impl.LuaJEnvironment;
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
        ModuleRegistry registry = ModuleRegistry.getInstance();
        registry.register(new RecipeModule());
        LuaValue armandilloGlobal = LuaValue.tableOf();

        armandilloGlobal.set("module", new ArmandilloModuleFunction());
        armandilloGlobal.set("load_class", new ArmandilloLoadClass());

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

        luaEnvironment.registerGlobal("Armandillo", armandilloGlobal);
        System.out.println("[Armandillo] Loader initialized.");
    }

    @Override
    public ILuaEnvironment getEnvironment() {
        return this.luaEnvironment;
    }
}