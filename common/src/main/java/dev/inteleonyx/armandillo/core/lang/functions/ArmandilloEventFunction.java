package dev.inteleonyx.armandillo.core.lang.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.objects.ArmandilloEvent;
import dev.inteleonyx.armandillo.core.registry.EventRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class ArmandilloEventFunction extends VarArgFunction {
    private final Map<String, List<LuaValue>> registeredCallbacks = new HashMap<>();

    @Override
    public Varargs invoke(Varargs args) {
        LuaValue eventNameLua = args.arg(1);
        LuaValue callback = args.arg(2);

        if (!eventNameLua.isstring()) {
            throw new IllegalArgumentException("First argument must be a string (event name)");
        }
        if (!callback.isfunction()) {
            throw new IllegalArgumentException("Second argument must be a function (callback)");
        }

        String eventName = eventNameLua.tojstring();

        ArmandilloEvent event = EventRegistry.getInstance()
                .getEvent(eventName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown event name: " + eventName));

        registeredCallbacks.computeIfAbsent(eventName, k -> new ArrayList<>()).add(callback);

        event.addLuaHandler(nativeEvent -> {
            List<LuaValue> callbacks = registeredCallbacks.get(eventName);
            if (callbacks == null) {
                return;
            }
            for (LuaValue func : callbacks) {
                try {
                    LuaValue luaEvent = convertNativeEventToLua(nativeEvent);
                    func.call(luaEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return LuaValue.NIL;
    }

    private LuaValue convertNativeEventToLua(Object nativeEvent) {
        return LuaValue.userdataOf(nativeEvent);
    }

    public void purgeEvents() {
        registeredCallbacks.clear();
    }
}

