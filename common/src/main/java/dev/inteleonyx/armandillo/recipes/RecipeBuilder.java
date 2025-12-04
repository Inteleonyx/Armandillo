package dev.inteleonyx.armandillo.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.utils.ItemEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class RecipeBuilder {
    private static JsonObject createIngredientObject(String rawItem) {
        JsonObject obj = new JsonObject();
        if (rawItem.startsWith("#")) {
            obj.addProperty("tag", rawItem.substring(1)); // Remove o '#'
        } else {
            obj.addProperty("item", rawItem);
        }
        return obj;
    }

    private static JsonObject createResultObject(String resultString) {
        ItemEntry entry = ItemEntry.parseItemAmount(resultString);
        JsonObject result = new JsonObject();
        result.addProperty("id", entry.itemId);
        result.addProperty("count", entry.count);
        return result;
    }

    public static ShapedRecipeBuilder shaped(String result) {
        return new ShapedRecipeBuilder(result);
    }

    public static ShapelessRecipeBuilder shapeless(String result) {
        return new ShapelessRecipeBuilder(result);
    }

    public static SmeltingRecipeBuilder blasting(String result) {
        return new SmeltingRecipeBuilder("minecraft:blasting", result);
    }

    public static SmeltingRecipeBuilder smoking(String result) {
        return new SmeltingRecipeBuilder("minecraft:smoking", result);
    }

    public static SmeltingRecipeBuilder campfire(String result) {
        return new SmeltingRecipeBuilder("minecraft:campfire_cooking", result);
    }

    public static StonecuttingRecipeBuilder stonecutting(String result) {
        return new StonecuttingRecipeBuilder(result);
    }

    public static SmithingRecipeBuilder smithingTransform(String result) {
        return new SmithingRecipeBuilder(result);
    }

    public static class ShapedRecipeBuilder {
        private final JsonObject recipe = new JsonObject();
        private final Map<String, String> keys = new HashMap<>();
        private String[] pattern;

        public ShapedRecipeBuilder(String result) {
            recipe.addProperty("type", "minecraft:crafting_shaped");
            recipe.addProperty("category", "misc");
            recipe.add("result", createResultObject(result));
        }

        /** Define o padrão de formato (pattern). */
        public ShapedRecipeBuilder pattern(String... pattern) {
            this.pattern = pattern;
            return this;
        }

        public ShapedRecipeBuilder key(String symbol, String rawItem) {
            keys.put(symbol, rawItem);
            return this;
        }

        public JsonObject build() {
            JsonObject keyObject = new JsonObject();
            keys.forEach((symbol, rawItem) -> {
                keyObject.add(symbol, createIngredientObject(rawItem));
            });
            recipe.add("key", keyObject);

            JsonArray patternArray = new JsonArray();
            for (String line : pattern) {
                patternArray.add(line);
            }
            recipe.add("pattern", patternArray);

            return recipe;
        }
    }

    public static class ShapelessRecipeBuilder {
        private final JsonObject recipe = new JsonObject();
        private final List<String> ingredients = new ArrayList<>();

        public ShapelessRecipeBuilder(String result) {
            recipe.addProperty("type", "minecraft:crafting_shapeless");
            recipe.addProperty("category", "misc");
            recipe.add("result", createResultObject(result));
        }

        public ShapelessRecipeBuilder ingredient(String rawItem) {
            ingredients.add(rawItem);
            return this;
        }

        public JsonObject build() {
            JsonArray ingredientsArray = new JsonArray();
            for (String rawIngredient : ingredients) {
                ItemEntry entry = ItemEntry.parseItemAmount(rawIngredient);

                for (int i = 0; i < entry.count; i++) {
                    String itemId = entry.itemId;
                    if (itemId.startsWith("#")) {
                        ingredientsArray.add(createIngredientObject(itemId));
                    } else {
                        ingredientsArray.add(createIngredientObject(itemId));
                    }
                }
            }
            recipe.add("ingredients", ingredientsArray);
            return recipe;
        }
    }

    public static class SmeltingRecipeBuilder {
        private final JsonObject recipe = new JsonObject();

        public SmeltingRecipeBuilder(String type, String result) {
            recipe.addProperty("type", type);
            recipe.addProperty("category", "misc");
            recipe.addProperty("group", "armandillo");

            ItemEntry entry = ItemEntry.parseItemAmount(result);
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("id", entry.itemId);
            recipe.add("result", resultObject);
        }

        public SmeltingRecipeBuilder ingredient(String rawItem) {
            recipe.add("ingredient", createIngredientObject(rawItem));
            return this;
        }

        public SmeltingRecipeBuilder time(int cookingTime) {
            recipe.addProperty("cookingtime", cookingTime);
            return this;
        }

        public SmeltingRecipeBuilder experience(float xp) {
            recipe.addProperty("experience", xp);
            return this;
        }

        public JsonObject build() {
            if (!recipe.has("cookingtime")) recipe.addProperty("cookingtime", 200);
            if (!recipe.has("experience")) recipe.addProperty("experience", 0.1f);

            return recipe;
        }
    }

    public static class StonecuttingRecipeBuilder {
        private final JsonObject recipe = new JsonObject();

        public StonecuttingRecipeBuilder(String result) {
            recipe.addProperty("type", "minecraft:stonecutting");
            recipe.add("result", createResultObject(result));
        }

        public StonecuttingRecipeBuilder ingredient(String rawItem) {
            recipe.add("ingredient", createIngredientObject(rawItem));
            return this;
        }

        public JsonObject build() {
            return recipe;
        }
    }

    public static class SmithingRecipeBuilder {
        private final JsonObject recipe = new JsonObject();

        public SmithingRecipeBuilder(String result) {
            recipe.addProperty("type", "minecraft:smithing_transform");
            recipe.add("result", createResultObject(result));
        }

        public SmithingRecipeBuilder template(String rawItem) {
            recipe.add("template", createIngredientObject(rawItem));
            return this;
        }

        public SmithingRecipeBuilder base(String rawItem) {
            recipe.add("base", createIngredientObject(rawItem));
            return this;
        }

        public SmithingRecipeBuilder addition(String rawItem) {
            recipe.add("addition", createIngredientObject(rawItem));
            return this;
        }

        public JsonObject build() {
            return recipe;
        }
    }
}
