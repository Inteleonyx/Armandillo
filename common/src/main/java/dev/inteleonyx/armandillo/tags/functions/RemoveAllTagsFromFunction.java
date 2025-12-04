package dev.inteleonyx.armandillo.tags.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.OneArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class RemoveAllTagsFromFunction extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue var1) {
        LuaTable table =  var1.checktable();
        String entryId = table.get(1).checkjstring();

        String criteria = String.format("ITEM_REMOVE_FROM_ALL:%s", entryId);
        RuntimeDataRegistry.addTagRemovalCriteria(criteria);
        return LuaValue.NIL;
    }
}
