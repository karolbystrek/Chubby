package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;

public class LiteralNode<T> extends ExpressionNode {

    private final T value;

    public LiteralNode(T value, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LiteralNode{" +
                "value=" + (value instanceof String ? "\"" + value + "\"" : value) +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiteralNode<?> that = (LiteralNode<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
