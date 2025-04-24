package com.karolbystrek.chubbycompiler.ast.expression.literal;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;

import java.util.Objects;


public abstract class LiteralNode<T> extends ExpressionNode {

    private final T value;


    protected LiteralNode(T value, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.value = value;
    }


    public T getValue() {
        return value;
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

    @Override
    public String toString() {
        return String.format("%s[%s @%d:%d]",
                getClass().getSimpleName().replace("LiteralNode", "Lit"), // Skrócona nazwa
                value, getLineNumber(), getColumnNumber());
    }
}