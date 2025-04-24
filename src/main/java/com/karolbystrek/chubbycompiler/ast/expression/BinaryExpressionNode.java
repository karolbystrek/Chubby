package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;


public class BinaryExpressionNode extends ExpressionNode {

    private final ExpressionNode leftOperand;
    private final String operator;
    private final ExpressionNode rightOperand;


    public BinaryExpressionNode(ExpressionNode leftOperand, String operator, ExpressionNode rightOperand,
                                int opLine, int opColumn) {
        super(opLine, opColumn);
        this.leftOperand = Objects.requireNonNull(leftOperand, "Left operand cannot be null");
        this.operator = Objects.requireNonNull(operator, "Operator cannot be null");
        this.rightOperand = Objects.requireNonNull(rightOperand, "Right operand cannot be null");
    }

    public ExpressionNode getLeftOperand() {
        return leftOperand;
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionNode getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return String.format("BinaryExpr[%s %s %s @%d:%d]",
                leftOperand, operator, rightOperand, getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return Objects.equals(leftOperand, that.leftOperand) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, operator, rightOperand);
    }
}