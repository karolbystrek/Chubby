package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;


public class IdentifierReferenceNode extends ExpressionNode {

    private final String identifierName;


    public IdentifierReferenceNode(String identifierName, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.identifierName = Objects.requireNonNull(identifierName, "Identifier name cannot be null");
    }


    public String getIdentifierName() {
        return identifierName;
    }

    @Override
    public String toString() {
        return String.format("IdRef[%s @%d:%d]", identifierName, getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierReferenceNode that = (IdentifierReferenceNode) o;
        return identifierName.equals(that.identifierName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifierName);
    }
}