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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class RecipeShapedFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {

        LuaTable recipeTable = args.checktable(1);
        LuaValue resultValue = recipeTable.get(1);
        String resultId = resultValue.checkjstring();
        ItemEntry entry = ItemEntry.parseItemAmount(resultId);
        String resultString = entry.itemId;

        LuaValue patternValue = recipeTable.get(2);
        if (patternValue.isnil()) {
            throw new IllegalArgumentException("Receita Shaped: Tabela de Pattern (segundo argumento) está faltando ou é nil.");
        }
        LuaTable patternTable = patternValue.checktable();
        List<String> patternList = new ArrayList<>();

        for (int i = 1; i <= patternTable.length(); i++) {
            LuaValue lineValue = patternTable.get(i);
            if (lineValue.isstring()) {
                patternList.add(lineValue.checkjstring());
            } else if (!lineValue.isnil()) {
                throw new IllegalArgumentException("Receita Shaped: A linha do padrão " + i + " deve ser uma string válida.");
            }
        }

        LuaValue keysValue = recipeTable.get(3);
        if (keysValue.isnil()) {
            throw new IllegalArgumentException("Receita Shaped: Tabela de Keys (terceiro argumento) está faltando ou é nil.");
        }
        LuaTable keysTable = keysValue.checktable();
        Map<String, String> keysMap = new HashMap<>();

        LuaValue key = LuaValue.NIL;
        while (true) {
            Varargs nextPair = keysTable.next(key);
            if (nextPair.isnil(1)) {
                break;
            }
            key = nextPair.arg(1);
            keysMap.put(key.checkjstring(), nextPair.arg(2).checkjstring());
        }

        RecipeBuilder.ShapedRecipeBuilder builder = RecipeBuilder.shaped(resultId);

        builder.pattern(patternList.toArray(new String[0]));

        keysMap.forEach(builder::key);

        JsonObject recipeJson = builder.build();

        System.out.println(recipeJson.toString());
        String path = resultString.replace(":", "_") + "_shaped_" + Integer.toHexString(recipeJson.hashCode());

        RuntimeRecipeRegistry.addData(ResourceLocation.fromNamespaceAndPath("armandillo", path), recipeJson);

        return LuaValue.NIL;
    }
}
