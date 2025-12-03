package dev.inteleonyx.armandillo.utils;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class ItemEntry {
    public final String itemId;
    public final int count;

    public ItemEntry(String itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public static ItemEntry parseItemAmount(String raw) {
        raw = raw.trim();

        if (raw.matches("^\\d+x\\s+.+")) {
            String[] split = raw.split("\\s+", 2);
            int amount = Integer.parseInt(split[0].replace("x", ""));
            String item = split[1];

            return new ItemEntry(item, amount);
        }

        return new ItemEntry(raw, 1);
    }
}
