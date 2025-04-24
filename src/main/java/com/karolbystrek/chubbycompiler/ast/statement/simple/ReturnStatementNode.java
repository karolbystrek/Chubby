package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class ReturnStatementNode extends StatementNode {

    private final ExpressionNode expression;

    public ReturnStatementNode(ExpressionNode expression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ReturnStatementNode{" +
                "expression=" + expression +
                '}';
    }
}
