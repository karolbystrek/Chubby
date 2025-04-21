package com.karolbystrek.chubbycompiler.ast;

public abstract class AstNode {

    private final int lineNumber;
    private final int columnNumber;

    public AstNode(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
