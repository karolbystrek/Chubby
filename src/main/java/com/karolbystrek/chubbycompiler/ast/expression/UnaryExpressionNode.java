package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;

public class UnaryExpressionNode extends ExpressionNode {

    public enum Operator {
        PLUS, MINUS, NOT
    }

    private final Operator operator;
    private final ExpressionNode operand;

    public UnaryExpressionNode(Operator operator, ExpressionNode operand, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.operator = Objects.requireNonNull(operator, "Operator cannot be null");
        this.operand = Objects.requireNonNull(operand, "Operand cannot be null");
    }

    public Operator getOperator() {
        return operator;
    }

    public ExpressionNode getOperand() {
        return operand;
    }

     @Override
    public String toString() {
        return "UnaryExpressionNode{" +
                "op=" + operator +
                ", operand=" + operand +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryExpressionNode that = (UnaryExpressionNode) o;
        return operator == that.operator && Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, operand);
    }
}
