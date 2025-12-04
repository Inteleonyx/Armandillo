package dev.inteleonyx.armandillo.recipes.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class RecipeRemoveFunction extends VarArgFunction {

    @Override
    public LuaValue invoke(Varargs args) {
        LuaTable table = args.checktable(1);

        String type = null;
        String value = null;

        if (!table.get("id").isnil()) {
            type = "id";
            value = table.get("id").checkjstring();
        } else if (!table.get("result").isnil()) {
            type = "result";
            value = table.get("result").checkjstring();
        } else if (!table.get("mod").isnil()) {
            type = "mod";
            value = table.get("mod").checkjstring();
        } else {
            throw new IllegalArgumentException("engine.remove needs to specify 'id', 'result' or 'mod'.");
        }

        String criteria = type.toLowerCase() + ":" + value;
        RuntimeDataRegistry.addRecipeRemovalCriteria(criteria);

        return LuaValue.NIL;
    }
}
