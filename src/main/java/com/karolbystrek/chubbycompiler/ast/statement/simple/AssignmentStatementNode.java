package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class AssignmentStatementNode extends StatementNode {

    public enum AssignmentOperator {
        ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, MULTIPLY_ASSIGN, DIVIDE_ASSIGN, MODULUS_ASSIGN,
    }

    private final LValueNode lValue;
    private final AssignmentOperatorNode operatorNode;
    private final ExpressionNode rValue;

    public AssignmentStatementNode(LValueNode lValue,
                                    AssignmentOperatorNode operatorNode,
                                    ExpressionNode rValue,
                                    int lineNumber,
                                    int columnNumber) {
        super(lineNumber, columnNumber);
        this.lValue = lValue;
        this.operatorNode = operatorNode;
        this.rValue = rValue;
    }

    public LValueNode getLValue() {
        return lValue;
    }

    public AssignmentOperatorNode getOperatorNode() {
        return operatorNode;
    }

    public AssignmentOperator getOperator() {
        return operatorNode.getOperator();
    }

    public ExpressionNode getRValue() {
        return rValue;
    }

    @Override
    public String toString() {
        return "AssignmentStatementNode{" +
                "lValue=" + lValue +
                ", operator=" + operatorNode.getOperator() +
                ", rValue=" + rValue +
                '}';
    }
}
