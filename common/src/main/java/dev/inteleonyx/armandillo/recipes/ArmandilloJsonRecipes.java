package dev.inteleonyx.armandillo.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.ArmandilloMod;
import dev.inteleonyx.armandillo.utils.ItemEntry;

import java.util.Map;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class ArmandilloJsonRecipes {
    public JsonObject shaped(String resultString, String[] pattern, Map<String, String> keys) {
        ItemEntry entry = ItemEntry.parseItemAmount(resultString);

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        JsonObject keyObject = new JsonObject();
        keys.forEach((symbol, rawItem) -> {
            ItemEntry keyEntry = ItemEntry.parseItemAmount(rawItem);

            JsonObject map = new JsonObject();
            if (keyEntry.itemId.startsWith("#")) {
                map.addProperty("tag", keyEntry.itemId);
            }
            else {
                map.addProperty("item", keyEntry.itemId);
            }

            keyObject.add(symbol, map);
        });
        recipe.add("key", keyObject);

        recipe.add("pattern", ArmandilloMod.getGSON().toJsonTree(pattern));

        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("id", entry.itemId);
        resultObject.addProperty("count", entry.count);
        recipe.add("result", resultObject);

        return recipe;
    }

    public JsonObject shapeless(String resultItem, String[] ingredients) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "misc");

        JsonArray ingredientsArray = new JsonArray();
        for (String rawIngredient : ingredients) {
            ItemEntry entry = ItemEntry.parseItemAmount(rawIngredient);

            for (int i = 0; i < entry.count; i++) {
                JsonObject ingredientObject = new JsonObject();

                String itemId = entry.itemId;

                if (itemId.startsWith("#")) {
                    ingredientObject.addProperty("tag", itemId.substring(1));
                } else {
                    ingredientObject.addProperty("item", itemId);
                }

                ingredientsArray.add(ingredientObject);
            }
        }

        recipe.add("ingredients", ingredientsArray);

        ItemEntry resultEntry = ItemEntry.parseItemAmount(resultItem);

        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("id", resultEntry.itemId);
        resultObject.addProperty("count", resultEntry.count);

        recipe.add("result", resultObject);

        return recipe;
    }

    public JsonObject furnace(String resultItem, String ingredient, int cookingTime, float xp) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:blasting");
        recipe.addProperty("category", "misc");
        recipe.addProperty("cookingtime", cookingTime);
        recipe.addProperty("experience", xp);
        recipe.addProperty("group", "armandillo");

        JsonObject ingredientObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            ingredientObject.addProperty("tag", ingredient);
        }
        else {
            ingredientObject.addProperty("item", ingredient);
        }
        recipe.add("ingredient", ingredientObject);

        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("id", resultItem);
        recipe.add("result", resultObject);

        return recipe;
    }

    public JsonObject campfire(String resultItem, String ingredient, int cookingTime, float xp) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:campfire_cooking");
        recipe.addProperty("category", "misc");
        recipe.addProperty("cookingtime", cookingTime);
        recipe.addProperty("experience", xp);
        recipe.addProperty("group", "armandillo");

        JsonObject ingredientObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            ingredientObject.addProperty("tag", ingredient);
        }
        else {
            ingredientObject.addProperty("item", ingredient);
        }
        recipe.add("ingredient", ingredientObject);

        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("id", resultItem);
        recipe.add("result", resultObject);

        return recipe;
    }

    public JsonObject smoking(String resultItem, String ingredient, int cookingTime, float xp) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:smoking");
        recipe.addProperty("category", "misc");
        recipe.addProperty("cookingtime", cookingTime);
        recipe.addProperty("experience", xp);
        recipe.addProperty("group", "armandillo");

        JsonObject ingredientObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            ingredientObject.addProperty("tag", ingredient);
        }
        else {
            ingredientObject.addProperty("item", ingredient);
        }
        recipe.add("ingredient", ingredientObject);

        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("id", resultItem);
        recipe.add("result", resultObject);

        return recipe;
    }

    public JsonObject smithing(String resultItem, String baseItem, String ingredient, String template) {
        ItemEntry entry = ItemEntry.parseItemAmount(resultItem);

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:smithing_transform");

        JsonObject ingredientObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            ingredientObject.addProperty("tag", ingredient);
        }
        else {
            ingredientObject.addProperty("item", ingredient);
        }
        recipe.add("addition", ingredientObject);

        JsonObject baseObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            baseObject.addProperty("tag", baseItem);
        }
        else {
            baseObject.addProperty("item", baseItem);
        }
        recipe.add("base", ingredientObject);

        JsonObject resultObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            resultObject.addProperty("tag", entry.itemId);
        }
        else {
            resultObject.addProperty("item", entry.itemId);
        }
        resultObject.addProperty("count", entry.count);
        recipe.add("result", resultObject);

        JsonObject templateObject = new JsonObject();
        if (template.startsWith("#")) {
            templateObject.addProperty("tag", template);
        }
        else {
            templateObject.addProperty("item", template);
        }
        recipe.add("template", templateObject);

        return recipe;
    }

    public JsonObject stonecutting(String resultItem, String ingredient) {
        ItemEntry entry = ItemEntry.parseItemAmount(resultItem);

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:stonecutting");

        JsonObject ingredientObject = new JsonObject();
        if (ingredient.startsWith("#")) {
            ingredientObject.addProperty("tag", ingredient);
        }
        else {
            ingredientObject.addProperty("item", ingredient);
        }
        recipe.add("ingredient", ingredientObject);

        JsonObject resultObject = new JsonObject();
        resultObject.addProperty("count", entry.count);
        resultObject.addProperty("id", entry.itemId);
        recipe.add("result", resultObject);

        return recipe;
    }
}
