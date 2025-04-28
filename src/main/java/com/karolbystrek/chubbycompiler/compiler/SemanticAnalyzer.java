package com.karolbystrek.chubbycompiler.compiler;

import com.karolbystrek.chubbycompiler.ast.ProgramNode;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;

/**
 * Performs semantic analysis on the Abstract Syntax Tree (AST).
 * This includes tasks like type checking, scope resolution, and ensuring
 * language rules are followed beyond basic syntax.
 */
public class SemanticAnalyzer {

    /**
     * Analzyes the entire program represented by the AST.
     *
     * @param programNode The root node of the AST.
     * @throws CompilationException if semantic errors are found.
     */
    public void analyze(ProgramNode programNode) throws CompilationException {
        // TODO: Implement semantic analysis logic
        // - Symbol token creation
        // - Type checking
        // - Scope resolution
        // - Checking variable initialization, function calls, etc.
    }
}
