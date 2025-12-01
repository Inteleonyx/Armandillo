package dev.inteleonyx.armandillo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class ScriptFinder {
    private static final String SCRIPT_FOLDER_NAME = "scripts";
    private static final String SCRIPT_EXTENSION = ".lua";

    public static List<String> find(Path armandilloRootPath) {
        if (armandilloRootPath == null) {
            System.err.println("[Armandillo] ❌ Cannot find scripts because root path is NULL!");
            return List.of();
        }

        Path scriptsDir = armandilloRootPath.resolve("scripts");

        if (!Files.isDirectory(scriptsDir)) {
            System.err.println("[Armandillo] ❌ Scripts folder missing at: " + scriptsDir);
            return List.of();
        }

        try (Stream<Path> paths = Files.walk(scriptsDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".lua"))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
