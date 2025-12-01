package dev.inteleonyx.armandillo.core;

import dev.inteleonyx.armandillo.luaj.ILuaEnvironment;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */
public interface IArmandilloLoader {
    void initialize();

    ILuaEnvironment getEnvironment();
}
