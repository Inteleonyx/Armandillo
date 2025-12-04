package dev.inteleonyx.armandillo.loot_table;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class LootTableBuilder {
    private final JsonObject root = new JsonObject();
    private final JsonArray pools = new JsonArray();
    private final List<LootPoolBuilder> poolBuilders = new ArrayList<>();

    public LootTableBuilder(String type) {
        root.addProperty("type", type);
        root.add("pools", pools);
    }

    public LootPoolBuilder addPool(String name) {
        LootPoolBuilder builder = new LootPoolBuilder(this, name);
        poolBuilders.add(builder);
        return builder;
    }
    public LootTableBuilder removePool(String name) {
        poolBuilders.removeIf(p -> p.poolObj.get("name").getAsString().equals(name));
        return this;
    }

    public JsonObject build() {
        pools.forEach(p -> pools.remove(p));
        poolBuilders.forEach(p -> pools.add(p.build()));
        return root;
    }

    public static JsonObject range(Number min, Number max) {
        JsonObject range = new JsonObject();
        range.addProperty("min", min);
        range.addProperty("max", max);
        return range;
    }

    public static JsonObject itemOrTag(String itemOrTag) {
        JsonObject obj = new JsonObject();
        if (itemOrTag.startsWith("#")) {
            obj.addProperty("tag", itemOrTag.substring(1));
        } else {
            obj.addProperty("item", itemOrTag);
        }
        return obj;
    }

    public static class LootPoolBuilder {
        private final LootTableBuilder parent;
        private final JsonObject poolObj = new JsonObject();
        private final JsonArray entries = new JsonArray();
        private final JsonArray conditions = new JsonArray();
        private final List<LootEntryBuilder> entryBuilders = new ArrayList<>();

        public LootPoolBuilder(LootTableBuilder parent, String name) {
            this.parent = parent;
            poolObj.addProperty("name", name);
            poolObj.add("entries", entries);
            poolObj.add("conditions", conditions); // Opcional
        }

        public LootPoolBuilder rolls(Number min, Number max) {
            poolObj.add("rolls", range(min, max));
            return this;
        }

        public LootPoolBuilder bonusRolls(Number min, Number max) {
            poolObj.add("bonus_rolls", range(min, max));
            return this;
        }

        public LootPoolBuilder addCondition(String conditionType, JsonObject params) {
            JsonObject condition = new JsonObject();
            condition.addProperty("condition", conditionType);
            if (params != null) {
                params.entrySet().forEach(entry -> condition.add(entry.getKey(), entry.getValue()));
            }
            conditions.add(condition);
            return this;
        }

        public LootEntryBuilder addEntry(String name, String type) {
            LootEntryBuilder builder = new LootEntryBuilder(this, name, type);
            entryBuilders.add(builder);
            return builder;
        }

        public LootPoolBuilder removeEntry(String name) {
            entryBuilders.removeIf(e -> e.entryObj.get("name").getAsString().equals(name));
            return this;
        }

        public LootTableBuilder endPool() {
            return parent;
        }

        public JsonObject build() {
            entries.forEach(e -> entries.remove(e));
            entryBuilders.forEach(e -> entries.add(e.build()));
            return poolObj;
        }
    }

    public static class LootEntryBuilder {
        private final LootPoolBuilder parent;
        private final JsonObject entryObj = new JsonObject();
        private final JsonArray functions = new JsonArray();
        private final JsonArray conditions = new JsonArray();

        public LootEntryBuilder(LootPoolBuilder parent, String name, String type) {
            this.parent = parent;
            entryObj.addProperty("name", name);
            entryObj.addProperty("type", type);
            entryObj.add("functions", functions);
            entryObj.add("conditions", conditions); // Opcional
        }

        public LootEntryBuilder weight(int weight) {
            entryObj.addProperty("weight", weight);
            return this;
        }

        public LootEntryBuilder count(Number min, Number max) {
            JsonObject countFunc = new JsonObject();
            countFunc.addProperty("function", "minecraft:set_count");

            if (min.equals(max)) {
                countFunc.addProperty("count", min);
            } else {
                countFunc.add("count", range(min, max));
            }

            functions.add(countFunc);
            return this;
        }

        public LootEntryBuilder addCondition(String conditionType, JsonObject params) {
            JsonObject condition = new JsonObject();
            condition.addProperty("condition", conditionType);
            if (params != null) {
                params.entrySet().forEach(entry -> condition.add(entry.getKey(), entry.getValue()));
            }
            conditions.add(condition);
            return this;
        }

        public LootEntryBuilder addFunction(String functionType, JsonObject params) {
            JsonObject func = new JsonObject();
            func.addProperty("function", functionType);
            if (params != null) {
                params.entrySet().forEach(entry -> func.add(entry.getKey(), entry.getValue()));
            }
            functions.add(func);
            return this;
        }

        public LootEntryBuilder enchantWith(int levels) {
            JsonObject params = new JsonObject();
            params.addProperty("levels", levels);
            return addFunction("minecraft:enchant_with_levels", params);
        }

        public LootPoolBuilder endEntry() {
            return parent;
        }

        public JsonObject build() {
            return entryObj;
        }
    }
}
