package dev.inteleonyx.armandillo;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import dev.inteleonyx.armandillo.commands.ArmandilloCommand;
import dev.inteleonyx.armandillo.core.impl.ArmandilloLoader;
import dev.inteleonyx.armandillo.utils.FolderSetup;
import lombok.Getter;

import java.nio.file.Path;

public final class ArmandilloMod {
    public static final String MOD_ID = "armandillo";
    public static Path ARMANDILLO_ROOT_PATH;

    @Getter
    public final ArmandilloLoader loader;

    @Getter
    private static ArmandilloMod INSTANCE;

    // ❗ Não chame init no construtor estático
    public ArmandilloMod() {
        INSTANCE = this; // ✅ define a instância antes de tudo
        this.loader = new ArmandilloLoader();

        // ✅ Agora sim podemos usar getGameFolder()
        Path baseDir = Platform.getGameFolder();
        if (baseDir == null) {
            System.err.println("[Armandillo] ❌ Platform.getGameFolder() returned NULL!");
            return;
        }

        ARMANDILLO_ROOT_PATH = FolderSetup.setupFolders(baseDir);

        if (ARMANDILLO_ROOT_PATH == null) {
            System.err.println("[Armandillo] ❌ Failed to setup root folder.");
            return;
        }

        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> {
            new ArmandilloCommand(this.loader).register(dispatcher);
        });

        // ✅ Finalmente inicializamos o loader
        this.loader.initialize();
        System.out.println("[Armandillo] ✅ Mod initialized successfully!");
    }
}
