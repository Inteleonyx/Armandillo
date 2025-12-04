package dev.inteleonyx.armandillo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
        loader.reloadScripts();

        CommandSourceStack serverSource = context.getSource().getServer()
                .createCommandSourceStack()
                .withPermission(4);

        try {
            context.getSource().getServer().getCommands().getDispatcher().execute(
                    context.getSource().getServer().getCommands().getDispatcher().parse("reload", serverSource)
            );

            context.getSource().sendSuccess(() -> Component.literal("Reloaded!"), true);

        } catch (CommandSyntaxException e) {
            System.err.println("Error when executing /reload: " + e.getMessage());
            context.getSource().sendFailure(Component.literal("Erro ao recarregar o servidor: " + e.getMessage()));
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal("Reloaded!"), true);
        return 1; // sucesso
    }
}
