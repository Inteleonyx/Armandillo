package dev.inteleonyx.armandillo.lang.modules.recipes.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.OneArgFunction;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class RecipeRemoveFunction extends OneArgFunction {
    private final dev.inteleonyx.armandillo.lang.modules.recipes.RecipeModule module;

    public RecipeRemoveFunction(dev.inteleonyx.armandillo.lang.modules.recipes.RecipeModule module) {
        this.module = module;
    }

    @Override
    public LuaValue call(LuaValue arg) {

        return LuaValue.NIL;
    }
}
