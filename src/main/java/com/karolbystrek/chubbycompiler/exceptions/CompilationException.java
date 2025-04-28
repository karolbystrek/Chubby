package com.karolbystrek.chubbycompiler.exceptions;

/**
 * Custom exception class for reporting errors during compilation (semantic analysis, code generation).
 */
public class CompilationException extends RuntimeException {

    public CompilationException(String message) {
        super(message);
    }

    public CompilationException(String message, int line, int column) {
        super(String.format("Error at %d:%d: %s", line, column, message));
    }

    public CompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
