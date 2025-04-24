package com.karolbystrek.chubbycompiler.ast;

import java.util.Objects;

public class ImportStatementNode extends AstNode {

    private final String qualifiedIdentifier;

    public ImportStatementNode(String qualifiedIdentifier, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.qualifiedIdentifier = Objects.requireNonNull(qualifiedIdentifier, "Qualified identifier cannot be null");
    }

    public String getQualifiedIdentifier() {
        return qualifiedIdentifier;
    }

    @Override
    public String toString() {
        return "ImportStatementNode{" +
                "qualifiedIdentifier='" + qualifiedIdentifier + '\'' +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportStatementNode that = (ImportStatementNode) o;
        return Objects.equals(qualifiedIdentifier, that.qualifiedIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedIdentifier);
    }
}
