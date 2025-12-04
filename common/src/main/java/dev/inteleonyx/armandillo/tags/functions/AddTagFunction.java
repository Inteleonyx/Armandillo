package dev.inteleonyx.armandillo.tags.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;

import static dev.inteleonyx.armandillo.tags.TagBuilder.addEntryToTag;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class AddTagFunction extends VarArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        LuaTable args = arg.checktable();
        String tagId = args.get(1).checkjstring();
        String itemId = args.get(2).checkjstring();

        addEntryToTag(tagId, itemId);
        return LuaValue.NIL;
    }
}
