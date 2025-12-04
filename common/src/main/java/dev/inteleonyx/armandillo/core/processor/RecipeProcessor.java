package dev.inteleonyx.armandillo.core.processor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Inteleonyx. Created on 04/12/2025
 * @project armandillo
 */

public class RecipeProcessor {
    public static void processRecipes(Map<ResourceLocation, JsonElement> map) {
        applyRemovals(map);
        injectNewRecipes(map);
    }

    private static void applyRemovals(Map<ResourceLocation, JsonElement> map) {
        Set<ResourceLocation> recipesToRemove = new HashSet<>();

        for (String criteria : RuntimeDataRegistry.getRecipeRemovalCriteria()) {

            String[] parts = criteria.split(":", 2);
            if (parts.length != 2) continue;

            String type = parts[0];
            String value = parts[1];

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                ResourceLocation id = entry.getKey();
                JsonElement json = entry.getValue();

                boolean match = false;

                if (type.equals("id") && id.toString().equals(value)) {
                    match = true;
                } else if (type.equals("mod") && id.getNamespace().equals(value)) {
                    match = true;
                } else if (type.equals("result")) {
                    if (json.isJsonObject() && Objects.equals(getRecipeResultId(json.getAsJsonObject()), value)) {
                        match = true;
                    }
                }

                if (match) {
                    recipesToRemove.add(id);
                }
            }
        }

        for (ResourceLocation id : recipesToRemove) {
            map.remove(id);
        }
    }

    private static String getRecipeResultId(JsonObject json) {
        if (json.has("result")) {
            JsonElement resultElement = json.get("result");

            if (resultElement.isJsonObject()) {
                JsonObject resultObj = resultElement.getAsJsonObject();

                if (resultObj.has("item")) {
                    return resultObj.get("item").getAsString();
                } else if (resultObj.has("id")) {
                    return resultObj.get("id").getAsString();
                }
            }
            else if (resultElement.isJsonPrimitive()) {
                return resultElement.getAsString();
            }
        }

        if (json.has("output") && json.get("output").isJsonPrimitive()) {
            return json.get("output").getAsString();
        }

        return "";
    }

    private static void injectNewRecipes(Map<ResourceLocation, JsonElement> map) {
        map.putAll(RuntimeDataRegistry.getAllRecipeData());
    }
}
