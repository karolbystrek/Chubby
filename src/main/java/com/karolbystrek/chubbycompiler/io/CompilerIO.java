// filepath: /Users/karol/Programming/Java/TKiK/ChubbyCompiler/src/main/java/com/karolbystrek/chubbycompiler/io/CompilerIO.java
package com.karolbystrek.chubbycompiler.io;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Utility class for handling compiler input and output operations.
 */
public final class CompilerIO {

    private static final String CLASS_FILE_EXTENSION = ".class";
    private static final String CBB_FILE_EXTENSION = ".cbb";

    private CompilerIO() {}

    /**
     * Reads the source file content into a CharStream.
     * @param inputFilePath Path to the source file.
     * @return CharStream representing the file content.
     * @throws IOException If an error occurs reading the file.
     */
    public static CharStream readSourceFile(String inputFilePath) throws IOException {
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            throw new IllegalArgumentException("Input file path cannot be null or empty.");
        }
        if (!inputFilePath.endsWith(CBB_FILE_EXTENSION)) {
            throw new IllegalArgumentException("Input file must end with a '.cbb' extension.");
        }

        return CharStreams.fromFileName(inputFilePath);
    }

    /**
     * Writes the compiled bytecode to .class files in the specified directory.
     *
     * @param compiledClasses Map of class names to bytecode arrays.
     * @param outputDirectory The directory to write the .class files to.
     * @throws IOException If an error occurs during file writing.
     */
    public static void writeClassFiles(Map<String, byte[]> compiledClasses, String outputDirectory) throws IOException {
        Path outputDirPath = Paths.get(outputDirectory);
        if (!Files.exists(outputDirPath)) {
             Files.createDirectories(outputDirPath);
        }

        for (Map.Entry<String, byte[]> entry : compiledClasses.entrySet()) {
            String className = entry.getKey();
            byte[] bytecode = entry.getValue();

            Path outputPath = outputDirPath.resolve(className.replace('.', File.separatorChar) + CLASS_FILE_EXTENSION);

            Path parentDir = outputPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            try (FileOutputStream stream = new FileOutputStream(outputPath.toFile())) {
                stream.write(bytecode);
            }
        }
    }
}
