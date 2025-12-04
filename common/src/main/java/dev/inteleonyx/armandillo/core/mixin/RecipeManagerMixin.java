package dev.inteleonyx.armandillo.core.mixin;

import com.google.gson.JsonElement;
import dev.inteleonyx.armandillo.core.processor.RecipeProcessor;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    protected void armandillo$injectRuntimeRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        RecipeProcessor.processRecipes(map);

        RuntimeDataRegistry.clearRecipeCache();
    }
}
