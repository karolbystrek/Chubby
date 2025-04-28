package com.karolbystrek.chubbycompiler.compiler;

import java.util.Map;

import com.karolbystrek.chubbycompiler.ast.ProgramNode;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;

/**
 * Orchestrates the compilation process, including semantic analysis and code generation.
 */
public class Compiler {

    private final SemanticAnalyzer semanticAnalyzer;
    private final CodeGenerator codeGenerator;

    public Compiler() {
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.codeGenerator = new CodeGenerator();
    }

    /**
     * Compiles the given AST into JVM bytecode.
     *
     * @param ast The root node of the Abstract Syntax Tree.
     * @return A map where keys are class names and values are the corresponding bytecode arrays.
     * @throws CompilationException if any compilation error occurs.
     */
    public Map<String, byte[]> compile(ProgramNode ast) throws CompilationException {
        semanticAnalyzer.analyze(ast);

        return codeGenerator.generate(ast);
    }
}
