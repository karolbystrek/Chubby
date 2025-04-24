package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;


public class ThisReferenceNode extends ExpressionNode {

    public ThisReferenceNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("ThisRef[@%d:%d]", getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}