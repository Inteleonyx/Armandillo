package dev.inteleonyx.armandillo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import dev.inteleonyx.armandillo.core.IArmandilloLoader;
import net.minecraft.network.chat.Component;

public class ArmandilloCommand {

    private final IArmandilloLoader loader;

    public ArmandilloCommand(IArmandilloLoader loader) {
        this.loader = loader;
    }

    public void registerCommand() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> {
            new ArmandilloCommand(loader).register(dispatcher);
        });
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("armandillo")
                .requires(source -> source.hasPermission(2)) // só ops podem executar
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2)) // só ops podem executar
                        .executes(this::reloadScripts)
                )
        );
    }

    private int reloadScripts(CommandContext<CommandSourceStack> context) {
        loader.reloadScripts();  // chama o método que recarrega os scripts Lua

        context.getSource().sendSuccess(() -> Component.translatable("armandillo.command.reload.success"), true);
        return 1; // sucesso
    }
}
