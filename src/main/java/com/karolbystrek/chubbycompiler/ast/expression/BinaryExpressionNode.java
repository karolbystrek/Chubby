package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;

public class BinaryExpressionNode extends ExpressionNode {

    public enum Operator {
        OR, AND,
        EQUAL, NOT_EQUAL,
        LESS, GREATER, LESS_EQUAL, GREATER_EQUAL,
        PLUS, MINUS,
        MULTIPLY, DIVIDE, MODULO
    }

    private final ExpressionNode leftOperand;
    private final Operator operator;
    private final ExpressionNode rightOperand;

    public BinaryExpressionNode(ExpressionNode leftOperand, Operator operator, ExpressionNode rightOperand, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.leftOperand = Objects.requireNonNull(leftOperand, "Left operand cannot be null");
        this.operator = Objects.requireNonNull(operator, "Operator cannot be null");
        this.rightOperand = Objects.requireNonNull(rightOperand, "Right operand cannot be null");
    }

    public ExpressionNode getLeftOperand() {
        return leftOperand;
    }

    public Operator getOperator() {
        return operator;
    }

    public ExpressionNode getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{" +
                "left=" + leftOperand +
                ", op=" + operator +
                ", right=" + rightOperand +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return Objects.equals(leftOperand, that.leftOperand) && operator == that.operator && Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, operator, rightOperand);
    }
}
