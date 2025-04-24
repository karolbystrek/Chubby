package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class ExpressionStatementNode extends StatementNode {

    private final ExpressionNode expression;

    public ExpressionStatementNode(ExpressionNode expression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ExpressionStatementNode{" +
               "expression=" + expression +
               '}';
    }
}
