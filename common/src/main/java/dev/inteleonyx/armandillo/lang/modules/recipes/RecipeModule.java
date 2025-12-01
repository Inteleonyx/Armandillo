package dev.inteleonyx.armandillo.lang.modules.recipes;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.IArmandilloModule;
import dev.inteleonyx.armandillo.lang.modules.recipes.functions.RecipeRemoveFunction;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class RecipeModule implements IArmandilloModule {
    private final LuaTable engine;

    public RecipeModule() {
        // Criamos a tabela 'engine' e populamos com as funções
        this.engine = new LuaTable();

        // Registra as funções que o script vai usar: engine.remove, engine.shaped
        this.engine.set("remove", new RecipeRemoveFunction(this));
        //this.engine.set("shaped", new ShapedFunction());
        // this.engine.set("shapeless", ...);
    }

    @Override
    public String getModuleId() {
        return "recipes";
    }

    @Override
    public LuaValue getModuleEngine() {
        return this.engine;
    }

    @Override
    public void onPostLoad() {
        // Aqui você poderia disparar o reload das receitas do Minecraft se necessário
        System.out.println("[Armandillo] Receitas processadas.");
    }
}
