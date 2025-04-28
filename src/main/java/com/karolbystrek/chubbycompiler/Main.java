package com.karolbystrek.chubbycompiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import com.karolbystrek.chubbycompiler.ast.ProgramNode;
import com.karolbystrek.chubbycompiler.compiler.Compiler;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;
import com.karolbystrek.chubbycompiler.parser.AstBuilderVisitor;
import com.karolbystrek.chubbycompiler.parser.DescriptiveErrorListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java -jar ChubbyCompiler.jar <input_file.cbb> [outputdirectory]");
            return;
        }

        String inputFilePath = args[0];
        String outputDirectory = (args.length > 1) ? args[1] : ".";

        File chubbyFile = new File(inputFilePath);
        if (!chubbyFile.exists()) {
            System.err.println("Error: Input file not found: " + inputFilePath);
            return;
        }

        try {
            CharStream input = CharStreams.fromFileName(inputFilePath);

            ChubbyParser parser = getChubbyParser(input);
            ParseTree tree = parser.program();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                System.err.println("Parsing failed due to syntax errors.");
                return;
            }

            AstBuilderVisitor astBuilder = new AstBuilderVisitor();
            AstNode astRoot = astBuilder.visit(tree);

            if (!(astRoot instanceof ProgramNode programNode)) {
                System.err.println("Error: AST root is not a ProgramNode.");
                return;
            }

            Compiler compiler = new Compiler();
            Map<String, byte[]> compiledClasses = compiler.compile(programNode);

            if (compiledClasses.isEmpty()) {
                System.err.println("No bytecode generated.");
            } else {
                writeClassFiles(compiledClasses, outputDirectory);
            }

        } catch (IOException e){
            System.err.println("Error reading or writing file: " + e.getMessage());
        } catch (RecognitionException e) {
            System.err.println("Recognition error during parsing: " + e.getMessage());
        } catch (CompilationException e) {
            System.err.println("Compilation error: " + e.getMessage());
        }  catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ChubbyParser getChubbyParser(CharStream input) {
        ChubbyLexer lexer = new ChubbyLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ChubbyParser parser = new ChubbyParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);

        return parser;
    }

    private static void writeClassFiles(Map<String, byte[]> compiledClasses, String outputDirectory) throws IOException {
        for (Map.Entry<String, byte[]> entry : compiledClasses.entrySet()) {
            String className = entry.getKey();
            byte[] bytecode = entry.getValue();

            Path outputPath = Paths.get(outputDirectory, className.replace('.', File.separatorChar) + ".class");
            File outputFile = outputPath.toFile();
            outputFile.getParentFile().mkdirs();

            try (FileOutputStream stream = new FileOutputStream(outputFile)) {
                stream.write(bytecode);
            }
        }
    }
}
