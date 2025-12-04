package dev.inteleonyx.armandillo.mixin;

import com.google.gson.JsonElement;
import dev.inteleonyx.armandillo.core.registry.RuntimeRecipeRegistry;
import dev.inteleonyx.armandillo.utils.RemoveRecipe;
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
    protected void injectRuntimeRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        RemoveRecipe.applyRemovals(map);

        Map<ResourceLocation, JsonElement> generatedRecipes = (Map) RuntimeRecipeRegistry.getAllData();
        map.putAll(generatedRecipes);
    }
}
