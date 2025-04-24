package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.AstNode;

public class AssignmentOperatorNode extends AstNode {
    private final AssignmentStatementNode.AssignmentOperator operator;

    public AssignmentOperatorNode(AssignmentStatementNode.AssignmentOperator operator, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.operator = operator;
    }

    public AssignmentStatementNode.AssignmentOperator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "AssignmentOperatorNode{" +
                "operator=" + operator +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }
}
