package dev.inteleonyx.armandillo.luaj;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */
public interface ILuaBinding {
    String getModuleName();

    void bind(dev.inteleonyx.armandillo.luaj.ILuaEnvironment environment);
}
