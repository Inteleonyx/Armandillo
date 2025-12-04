package dev.inteleonyx.armandillo.tags.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.TwoArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class RemoveTagFunction extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue var1, LuaValue var2) {
        LuaTable table =  var1.checktable();
        String entryId = table.get(2).checkjstring();
        String tagId = table.get(1).checkjstring();

        String criteria = String.format("TAG_REMOVE_ENTRY:%s:%s", tagId, entryId);
        RuntimeDataRegistry.addTagRemovalCriteria(criteria);

        return LuaValue.NIL;
    }
}
