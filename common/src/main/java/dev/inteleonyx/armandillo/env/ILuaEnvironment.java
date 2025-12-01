package dev.inteleonyx.armandillo.luaj;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */
public interface ILuaEnvironment {
    void registerGlobal(String name, LuaValue function);

    void callFunction(String name, Object... args);

    void runScript(String luaScript);
}
