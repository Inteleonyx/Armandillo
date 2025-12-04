package dev.inteleonyx.armandillo.registry;

import dev.inteleonyx.armandillo.core.objects.ArmandilloEvent;
import dev.inteleonyx.armandillo.core.objects.ArmandilloModule;
import dev.inteleonyx.armandillo.event.TickEvent;
import dev.inteleonyx.armandillo.recipes.RecipesArmandilloModule;
import dev.inteleonyx.armandillo.tags.TagsArmandilloModule;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class ArmandilloRegistries {
    public static final ArmandilloRegistry<ArmandilloModule> MODULES = new ArmandilloRegistry<>();
    public static final ArmandilloRegistry<ArmandilloEvent> EVENTS = new ArmandilloRegistry<>();

    static {
        //EVENTS
        EVENTS.register("tick", TickEvent::new);

        //MODULES
        MODULES.register("recipes", RecipesArmandilloModule::new);
        MODULES.register("tags", TagsArmandilloModule::new);
    }

    private ArmandilloRegistries() {}
}
