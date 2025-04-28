package com.karolbystrek.chubbycompiler.compiler;

import java.util.Collections;
import java.util.Map;

import com.karolbystrek.chubbycompiler.ast.ProgramNode;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;

/**
 * Generates JVM bytecode from the Abstract Syntax Tree (AST).
 */
public class CodeGenerator {

    /**
     * Generates bytecode for all classes defined in the program AST.
     *
     * @param programNode The root node of the AST.
     * @return A map where keys are fully qualified class names and values are bytecode arrays.
     * @throws CompilationException if errors occur during code generation.
     */
    public Map<String, byte[]> generate(ProgramNode programNode) throws CompilationException {
        // TODO: Implement bytecode generation using AST
        // - Iterate through ClassDefinitionNodes
        // - For each class, use ASM's ClassWriter, MethodVisitor, etc.
        // - Translate AST nodes into JVM instructions
        return Collections.emptyMap();
    }
}
