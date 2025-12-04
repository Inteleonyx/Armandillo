package dev.inteleonyx.armandillo.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class FolderSetup {
    private static final String ARMANDILLO_FOLDER_NAME = "armandillo";
    private static final String SCRIPTS_FOLDER = "scripts";
    private static final String CONFIG_FOLDER = "config";
    private static final String DATAPACK_FOLDER = "datapack";
    private static final String ASSETS_FOLDER = "assets";

    public static Path setupFolders(Path baseDir) {
        if (baseDir == null) {
            System.err.println("[Armandillo] ❌ Game directory (baseDir) is NULL! Cannot create folders.");
            return null;
        }

        // Montes o root final
        Path root = baseDir.resolve(ARMANDILLO_FOLDER_NAME);
        Path scripts = root.resolve(SCRIPTS_FOLDER);
        Path config = root.resolve(CONFIG_FOLDER);
        Path datapack = root.resolve(DATAPACK_FOLDER);
        Path assets = root.resolve(ASSETS_FOLDER);

        try {
            System.out.println("[Armandillo] Checking folder validity at: " + root.toAbsolutePath());

            if (!Files.exists(root)) {
                Files.createDirectories(root);
                System.out.println("[Armandillo] 📁 Root folder created.");
            }

            if (!Files.exists(scripts)) {
                Files.createDirectories(scripts);
                System.out.println("[Armandillo] 📜 Scripts folder created.");
            }

            //if (!Files.exists(config)) {
                //Files.createDirectories(config);
                //System.out.println("[Armandillo] ⚙️ Config folder created.");
            //}

            //if (!Files.exists(datapack)) {
                //Files.createDirectories(datapack);
                //System.out.println("[Armandillo] 🎒 Datapack folder created.");
            //}

            //if (!Files.exists(assets)) {
                //Files.createDirectories(assets);
                //System.out.println("[Armandillo] 🎨 Assets folder created.");
            //}

            System.out.println("[Armandillo] ✅ Folder structure verified/OK.");
            return root;

        } catch (IOException e) {
            System.err.println("[Armandillo] 💥 Failed to create directories!");
            e.printStackTrace();
            return null;
        }
    }
}
