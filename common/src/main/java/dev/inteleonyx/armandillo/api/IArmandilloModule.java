package dev.inteleonyx.armandillo.api;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */
public interface IArmandilloModule {

    String getModuleId();

    LuaValue getModuleEngine();

    default void onPostLoad() {}
}
