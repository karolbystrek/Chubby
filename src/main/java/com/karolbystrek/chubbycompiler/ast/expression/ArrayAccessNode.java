package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.statement.simple.LValueNode;

import java.util.Objects;

/**
 * Represents accessing an element of an array (e.g., arr[index]).
 * Functions as an LValue.
 */
public class ArrayAccessNode extends LValueNode {

    private final ExpressionNode arrayExpression;
    private final ExpressionNode indexExpression;

    public ArrayAccessNode(ExpressionNode arrayExpression, ExpressionNode indexExpression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
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
        return "ArrayAccessNode{" +
                "array=" + arrayExpression +
                ", index=" + indexExpression +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayAccessNode that = (ArrayAccessNode) o;
        return Objects.equals(arrayExpression, that.arrayExpression) && Objects.equals(indexExpression, that.indexExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrayExpression, indexExpression);
    }
}
