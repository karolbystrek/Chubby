package com.karolbystrek.chubbycompiler.ast;

import java.util.Objects;

public class ImportStatementNode extends AstNode {

    private final String identifier;

    public ImportStatementNode(String identifier, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.identifier = Objects.requireNonNull(identifier, "Qualified identifier cannot be null");
    }

    public String getidentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "ImportStatementNode{" +
                "identifier='" + identifier + '\'' +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportStatementNode that = (ImportStatementNode) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
