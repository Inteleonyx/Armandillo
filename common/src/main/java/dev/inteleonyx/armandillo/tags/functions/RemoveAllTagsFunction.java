package dev.inteleonyx.armandillo.tags.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.OneArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class RemoveAllTagsFunction extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue var1) {
        LuaTable tableArg = var1.checktable();
        String tagId = tableArg.get(1).checkjstring();

        String criteria = String.format("TAG_CLEAR_ENTRIES:%s", tagId);
        RuntimeDataRegistry.addTagRemovalCriteria(criteria);

        return LuaValue.NIL;
    }
}
