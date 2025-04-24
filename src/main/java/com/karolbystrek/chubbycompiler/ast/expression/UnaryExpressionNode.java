package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;


// np zmaian znaku lub zaprzecznie
public class UnaryExpressionNode extends ExpressionNode {

    private final String operator;
    private final ExpressionNode operand;


    public UnaryExpressionNode(String operator, ExpressionNode operand, int opLine, int opColumn) {
        super(opLine, opColumn);
        this.operator = Objects.requireNonNull(operator, "Operator cannot be null");
        this.operand = Objects.requireNonNull(operand, "Operand cannot be null");
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionNode getOperand() {
        return operand;
    }

    @Override
    public String toString() {
        return String.format("UnaryExpr[%s %s @%d:%d]",
                operator, operand, getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryExpressionNode that = (UnaryExpressionNode) o;
        return Objects.equals(operator, that.operator) && Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, operand);
    }
}