package dev.inteleonyx.armandillo.env.impl;

import dev.inteleonyx.armandillo.api.luaj.Globals;
import dev.inteleonyx.armandillo.api.luaj.LuaError;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.jse.JsePlatform;
import dev.inteleonyx.armandillo.luaj.ILuaEnvironment;
import lombok.Getter;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

@Getter
public class LuaJEnvironment implements ILuaEnvironment {
    private final Globals globals;

    public LuaJEnvironment() {
        this.globals = JsePlatform.standardGlobals();
    }

    @Override
    public void registerGlobal(String name, LuaValue value) {
        this.globals.set(name, value);
        System.out.println("[Armandillo] Global '" + name + "' registered.");
    }

    @Override
    public void runScript(String luaScript) {
        try {
            this.globals.load(luaScript).call();
        } catch (LuaError e) {
            System.err.println("[Armandillo] Runtime Lua error " + e.getMessage());
        }
    }

    @Override
    public void callFunction(String name, Object... args) {
        LuaValue function = this.globals.get(name);

        if (function.isnil()) {
            return;
        }

        LuaValue[] luaArgs = new LuaValue[args.length];
        for (int i = 0; i < args.length; i++) {
            luaArgs[i] = LuaValue.userdataOf(args[i]);
        }

        try {
            function.invoke(luaArgs);
        } catch (LuaError e) {
            System.err.println("[Armandillo] failed to call function '" + name + "': " + e.getMessage());
        }
    }

    public Globals getGlobals() {
        return this.globals;
    }
}
