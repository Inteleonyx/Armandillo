package dev.inteleonyx.armandillo.core.objects;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.inteleonyx.armandillo.utils.annotations.ArmandilloRuntime;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

@ArmandilloRuntime
public abstract class ArmandilloEvent {
    @Getter
    protected final String eventName;

    private final List<Consumer<Object>> luaHandlers = new ArrayList<>();

    protected ArmandilloEvent(String eventName) {
        this.eventName = eventName;
        LifecycleEvent.SERVER_STARTED.register(server -> listenGameEvent());
    }

    protected abstract void listenGameEvent();

    public void addLuaHandler(Consumer<Object> handler) {
        luaHandlers.add(handler);
    }

    public void removeLuaHandler(Consumer<Object> handler) {
        luaHandlers.remove(handler);
    }

    public void clearLuaHandlers() {
        luaHandlers.clear();
    }

    protected void dispatch(Object nativeEvent) {
        for (Consumer<Object> luaHandler : luaHandlers) {
            luaHandler.accept(nativeEvent);
        }
    }
}
