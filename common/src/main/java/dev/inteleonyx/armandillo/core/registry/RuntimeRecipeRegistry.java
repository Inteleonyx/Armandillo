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

public class RuntimeRecipeRegistry {
    private static final Map<ResourceLocation, JsonObject> DATA = new ConcurrentHashMap<>();

    private static final List<String> REMOVAL_CRITERIA = new ArrayList<>();

    public static void clear() {
        DATA.clear();
        REMOVAL_CRITERIA.clear();
    }

    public static void addData(ResourceLocation id, JsonObject json) {
        DATA.put(id, json);
        System.out.println("Registrei " + id);
    }

    public static Map<ResourceLocation, JsonObject> getAllData() {
        return DATA;
    }

    public static void addRemovalCriteria(String criteria) {
        REMOVAL_CRITERIA.add(criteria);
    }

    public static List<String> getRemovalCriteria() {
        return REMOVAL_CRITERIA;
    }

}
