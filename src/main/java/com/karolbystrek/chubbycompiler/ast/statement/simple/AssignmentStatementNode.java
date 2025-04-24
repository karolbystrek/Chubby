package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class AssignmentStatementNode extends StatementNode {

    public enum AssignmentOperator {
        ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, MULTIPLY_ASSIGN, DIVIDE_ASSIGN, MODULUS_ASSIGN,
    }

    private final LValueNode lValue;
    private final AssignmentOperator operator;
    private final ExpressionNode rValue;

    public AssignmentStatementNode(LValueNode lValue,
                                    AssignmentOperator operator,
                                    ExpressionNode rValue,
                                    int lineNumber,
                                    int columnNumber) {
        super(lineNumber, columnNumber);
        this.lValue = lValue;
        this.operator = operator;
        this.rValue = rValue;
    }

    public LValueNode getLValue() {
        return lValue;
    }

    public AssignmentOperator getOperator() {
        return operator;
    }

    public ExpressionNode getRValue() {
        return rValue;
    }

    @Override
    public String toString() {
        return "AssignmentStatementNode{" +
                "lValue=" + lValue +
                ", operator=" + operator +
                ", rValue=" + rValue +
                '}';
    }
}
