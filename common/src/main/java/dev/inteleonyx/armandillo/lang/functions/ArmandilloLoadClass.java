package dev.inteleonyx.armandillo.lang.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.OneArgFunction;
import dev.inteleonyx.armandillo.api.luaj.lib.jse.CoerceJavaToLua;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class ArmandilloLoadClass extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue arg) {
        if (!arg.isstring()) {
            System.err.println("[Armandillo] 'load_class()' expects a string");
            return LuaValue.NIL;
        }

        String className = arg.checkjstring();

        try {
            Class<?> clazz = Class.forName(className);

            LuaValue luaClass = CoerceJavaToLua.coerce(clazz);

            System.out.println("[Armandillo] Class loaded " + className);

            return luaClass;

        } catch (ClassNotFoundException e) {
            System.err.println("[Armandillo] Class not found: " + className);
            throw new LuaError("Class not found: " + className);
        } catch (Throwable e) {
            System.err.println("[Armandillo] Error loading class: " + className + ": " + e.getMessage());
            throw new LuaError("Error loading class: " + e.getMessage());
        }
    }
}
