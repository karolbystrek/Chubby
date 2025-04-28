package com.karolbystrek.chubbycompiler.compiler;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import com.karolbystrek.chubbycompiler.ast.ClassDefinitionNode;
import com.karolbystrek.chubbycompiler.ast.ProgramNode;
import com.karolbystrek.chubbycompiler.ast.Visibility;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;

/**
 * Generates JVM bytecode from the Abstract Syntax Tree (AST).
 */
public class CodeGenerator implements Opcodes {

    private static final int JAVA_VERSION = V21;

    public CodeGenerator() {}

    /**
     * Generates bytecode for all classes defined in the program AST.
     *
     * @param programNode The root node of the AST.
     * @return A map where keys are fully qualified class names and values are bytecode arrays.
     * @throws CompilationException if errors occur during code generation.
     */
    public Map<String, byte[]> generate(ProgramNode programNode) throws CompilationException {
        Map<String, byte[]> compiledCLasses = new HashMap<>();

        for(ClassDefinitionNode classDef : programNode.getClassDefinitions()) {
            try {
                byte[] bytecode = generateClass(classDef);

                compiledCLasses.put(classDef.getName(), bytecode);
            } catch (Exception e) {
                throw new CompilationException("Error generating bytecode for class " + classDef.getName() + ": " + e.getMessage(), e);
            }
        }

        if (compiledCLasses.isEmpty()) {
            System.err.println("No classes found in the AST to generate.");
        }

        return compiledCLasses;
    }

    /**
     * Generates the bytecode for a single class definition
     *
     * @param classDef The ClassDefinitionNode from the AST.
     * @return The bytecode for the class as a byte array.
     */
    private byte[] generateClass(ClassDefinitionNode classDefinition) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        String className = classDefinition.getName();
        String superClassName = "java/lang/Object";

        int accessFLags = mapVisibilityToAccessFlags(classDefinition.getVisibility());
        accessFLags |= ACC_SUPER;

        writer.visit(JAVA_VERSION, accessFLags, className, null, superClassName, null);

        // TODO: Visit fields, methods, etc.

        writer.visitEnd();
        return writer.toByteArray();
    }

    /**
     * Maps the Chubby Visibility enum to ASM access flags.
     *
     * @param visibility The visibility from the AST node.
     * @return The corresponding Opcodes access flag (e.g., ACC_PUBLIC).
     */
    private int mapVisibilityToAccessFlags(Visibility visibility) {
        return switch (visibility) {
            case PUBLIC -> ACC_PUBLIC;
            case PRIVATE -> ACC_PRIVATE;
            case PROTECTED -> ACC_PROTECTED;
            default -> ACC_PRIVATE;
        };
    }
}
