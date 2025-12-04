package dev.inteleonyx.armandillo.core.registry;

import dev.inteleonyx.armandillo.api.luaj.Globals;
import dev.inteleonyx.armandillo.core.objects.ArmandilloEvent;
import dev.inteleonyx.armandillo.core.lang.functions.ArmandilloEventFunction;
import dev.inteleonyx.armandillo.registry.ArmandilloRegistries;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class EventRegistry {
    private static final EventRegistry INSTANCE = new EventRegistry();

    @Getter
    Map<String, ArmandilloEvent> allEvents = ArmandilloRegistries.EVENTS.getAll();

    public static EventRegistry getInstance() {
        return INSTANCE;
    }

    private EventRegistry() {}


    public void register(Globals globals) {
        for (Map.Entry<String, ArmandilloEvent> entry : allEvents.entrySet()) {
            String eventName = entry.getKey();
            ArmandilloEvent event = entry.getValue();

            ArmandilloEventFunction function = new ArmandilloEventFunction();

            globals.set(eventName, function);
        }
    }

    public boolean unregister() {
        for (Map.Entry<String, ArmandilloEvent> entry : allEvents.entrySet()) {
            ArmandilloEvent event = entry.getValue();
            event.clearLuaHandlers();
            return true;
        }
        return false;
    }

    public Optional<ArmandilloEvent> getEvent(String eventName) {
        return Optional.ofNullable(allEvents.get(eventName));
    }
}
