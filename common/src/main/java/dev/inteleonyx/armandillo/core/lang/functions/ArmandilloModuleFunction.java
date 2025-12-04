package dev.inteleonyx.armandillo.core.lang.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaFunction;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.registry.ModuleRegistry;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class ArmandilloModuleFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {
        if (args.narg() < 2 || !args.isstring(1) || !args.isfunction(2)) {
            System.err.println("[Armandillo] Invalid syntax. Use: Armandillo.module('id', function)");
            return LuaValue.NIL;
        }

        String moduleId = args.checkjstring(1);
        LuaFunction callback = args.checkfunction(2);

        ModuleRegistry.getInstance().getModule(moduleId).ifPresentOrElse(module -> {
            try {
                LuaValue engine = module.getModuleEngine();
                callback.call(engine);
            } catch (Exception e) {
                System.err.println("[Armandillo] Error on module '" + moduleId + "': " + e.getMessage());
                e.printStackTrace();
            }
        }, () -> {
            System.err.println("[Armandillo]: Module '" + moduleId + "' wasn't found");
        });

        return LuaValue.NIL;
    }
}
