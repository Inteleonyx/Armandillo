package dev.inteleonyx.armandillo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Inteleonyx. Created on 04/12/2025
 * @project armandillo
 */

public class TagEntryConverter {
    public static Collection<TagEntry> convert(JsonObject tagJson) {
        if (!tagJson.has("values") || !tagJson.get("values").isJsonArray()) {
            throw new IllegalArgumentException("Tag JSON needs 'values'.");
        }

        JsonArray values = tagJson.getAsJsonArray("values");

        return values.asList().stream()
                .map(element -> {
                    String value = element.getAsString();

                    if (value.startsWith("#")) {
                        return TagEntry.tag(ResourceLocation.parse(value.substring(1)));
                    }
                    else {
                        return TagEntry.element(ResourceLocation.parse(value));
                    }
                })
                .collect(Collectors.toList());
    }
}
