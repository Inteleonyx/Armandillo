package dev.inteleonyx.armandillo.recipes;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.core.objects.ArmandilloModule;
import dev.inteleonyx.armandillo.recipes.functions.RecipeRemoveFunction;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class RecipesArmandilloModule extends ArmandilloModule {
    LuaValue engine;

    public RecipesArmandilloModule() {
        super("recipes");
    }

    @Override
    public LuaValue getModuleEngine() {
        return this.engine;
    }

    @Override
    public void init() {
        this.engine.set("remove", new RecipeRemoveFunction(this));
    }
}
