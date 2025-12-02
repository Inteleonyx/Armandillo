package dev.inteleonyx.armandillo.event;

import dev.inteleonyx.armandillo.core.objects.ArmandilloEvent;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class TickEvent extends ArmandilloEvent {
    public TickEvent() {
        super("tick");
    }

    @Override
    protected void listenGameEvent() {
        dev.architectury.event.events.common.TickEvent.SERVER_POST.register(this::dispatch);
    }
}
