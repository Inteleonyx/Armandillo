package dev.inteleonyx.armandillo.recipes;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.core.objects.ArmandilloModule;
import dev.inteleonyx.armandillo.recipes.functions.RecipeRemoveFunction;
import dev.inteleonyx.armandillo.recipes.functions.RecipeShapedFunction;
import dev.inteleonyx.armandillo.recipes.functions.RecipeShapelessFunction;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class RecipesArmandilloModule extends ArmandilloModule {
    LuaValue engine = LuaValue.tableOf();

    public RecipesArmandilloModule() {
        super("recipes");
    }

    @Override
    public LuaValue getModuleEngine() {
        return this.engine;
    }

    @Override
    public void init() {
        engine.set("shaped", new RecipeShapedFunction());
        engine.set("shapeless", new RecipeShapelessFunction());
        engine.set("remove", new RecipeRemoveFunction());
    }
}
