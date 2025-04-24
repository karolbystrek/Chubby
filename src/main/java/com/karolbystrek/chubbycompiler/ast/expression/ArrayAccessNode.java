package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;


public class ArrayAccessNode extends ExpressionNode {

    private final ExpressionNode arrayExpression;
    private final ExpressionNode indexExpression;


    public ArrayAccessNode(ExpressionNode arrayExpression, ExpressionNode indexExpression, int bracketLine, int bracketColumn) {
        super(bracketLine, bracketColumn);
        this.arrayExpression = Objects.requireNonNull(arrayExpression, "Array expression cannot be null");
        this.indexExpression = Objects.requireNonNull(indexExpression, "Index expression cannot be null");
    }

    public ExpressionNode getArrayExpression() {
        return arrayExpression;
    }

    public ExpressionNode getIndexExpression() {
        return indexExpression;
    }

    @Override
    public String toString() {
        return String.format("ArrayAccess[array=%s, index=%s @%d:%d]",
                arrayExpression, indexExpression, getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayAccessNode that = (ArrayAccessNode) o;
        return arrayExpression.equals(that.arrayExpression) && indexExpression.equals(that.indexExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrayExpression, indexExpression);
    }
}