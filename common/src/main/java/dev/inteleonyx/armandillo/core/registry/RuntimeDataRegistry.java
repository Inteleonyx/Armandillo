package dev.inteleonyx.armandillo.core.registry;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class RuntimeDataRegistry {
    private static final Map<ResourceLocation, JsonObject> RECIPE_DATA = new ConcurrentHashMap<>();
    private static final List<String> RECIPE_REMOVAL_CRITERIA = new ArrayList<>();

    private static final Map<ResourceLocation, JsonObject> TAG_DATA = new ConcurrentHashMap<>();
    private static final List<String> TAG_REMOVAL_CRITERIA = new ArrayList<>();

    public static void clearRecipeCache() {
        RECIPE_DATA.clear();
        RECIPE_REMOVAL_CRITERIA.clear();
    }

    public static void addRecipeData(ResourceLocation id, JsonObject json) {
        RECIPE_DATA.put(id, json);
    }

    public static Map<ResourceLocation, JsonObject> getAllRecipeData() {
        return RECIPE_DATA;
    }

    public static void addRecipeRemovalCriteria(String criteria) {
        RECIPE_REMOVAL_CRITERIA.add(criteria);
    }

    public static List<String> getRecipeRemovalCriteria() {
        return RECIPE_REMOVAL_CRITERIA;
    }

    public static void clearTagCache() {
        TAG_DATA.clear();
        TAG_REMOVAL_CRITERIA.clear();
    }

    public static void addTagData(ResourceLocation id, JsonObject json) {
        TAG_DATA.put(id, json);
    }

    public static Map<ResourceLocation, JsonObject> getAllTagData() {
        return TAG_DATA;
    }

    public static void addTagRemovalCriteria(String criteria) {
        TAG_REMOVAL_CRITERIA.add(criteria);
    }

    public static List<String> getTagRemovalCriteria() {
        return TAG_REMOVAL_CRITERIA;
    }
}
