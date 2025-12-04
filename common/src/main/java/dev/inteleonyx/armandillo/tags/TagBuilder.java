package dev.inteleonyx.armandillo.tags;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class TagBuilder {
    private static JsonObject getOrCreateTagJson(ResourceLocation tagId) {
        Map<ResourceLocation, JsonObject> tagData = RuntimeDataRegistry.getAllTagData();
        JsonObject tagJson = tagData.getOrDefault(tagId, new JsonObject());

        if (!tagJson.has("replace")) {
            tagJson.addProperty("replace", false);
        }

        if (!tagJson.has("values")) {
            tagJson.add("values", new JsonArray());
        }

        tagData.put(tagId, tagJson);

        return tagJson;
    }

    public static void addEntryToTag(String tagIdString, String entryIdString) {
        try {
            ResourceLocation tagId = ResourceLocation.parse(tagIdString);
            ResourceLocation entryId = ResourceLocation.parse(entryIdString);

            JsonObject tagJson = getOrCreateTagJson(tagId);
            JsonArray values = tagJson.getAsJsonArray("values");

            boolean exists = values.asList().stream()
                    .anyMatch(e -> e.getAsString().equals(entryId.toString()));

            if (!exists) {
                values.add(entryId.toString());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Entry ID invalid: " + entryIdString, e);
        }
    }
}
