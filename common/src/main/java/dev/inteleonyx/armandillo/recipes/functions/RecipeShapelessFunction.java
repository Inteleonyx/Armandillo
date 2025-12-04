package dev.inteleonyx.armandillo.recipes.functions;

import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeRecipeRegistry;
import dev.inteleonyx.armandillo.recipes.RecipeBuilder;
import dev.inteleonyx.armandillo.utils.ItemEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class RecipeShapelessFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {
        LuaTable table = args.checktable(1);

        // Resultado do craft (index 1)
        String resultId = table.get(1).checkjstring();
        ItemEntry entry = ItemEntry.parseItemAmount(resultId);
        String resultString = entry.itemId;

        // Ingredientes (index 2, deve ser um ARRAY/TABLE)
        LuaValue ingredientArrayValue = table.get(2);
        if (ingredientArrayValue.isnil()) {
            throw new IllegalArgumentException("shapeless: ingredient table missing");
        }

        LuaTable ingredientArray = ingredientArrayValue.checktable();

        List<String> ingredients = new ArrayList<>();
        Set<String> unique = new HashSet<>();

        for (int i = 1; i <= ingredientArray.length(); i++) {
            LuaValue v = ingredientArray.get(i);
            if (v.isstring()) {
                String ing = v.checkjstring();
                if (!unique.add(ing)) {
                    throw new IllegalArgumentException("shapeless: duplicated ingredient '" + ing + "'");
                }
                ingredients.add(ing);
            } else if (!v.isnil()) {
                throw new IllegalArgumentException("shapeless: ingredient must be string[]");
            }
        }

        // Builda a receita no jogo
        RecipeBuilder.ShapelessRecipeBuilder builder = new RecipeBuilder.ShapelessRecipeBuilder(resultId);
        for (String ing : ingredients) {
            builder.ingredient(ing);
        }

        JsonObject recipeJson = builder.build();

        System.out.println(recipeJson.toString());
        String path = resultString.replace(":", "_") + "_shapeless_" + Integer.toHexString(recipeJson.hashCode());

        RuntimeRecipeRegistry.addData(ResourceLocation.fromNamespaceAndPath("armandillo", path), recipeJson);

        return LuaValue.NIL;
    }
}
