package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class ThrowStatementNode extends StatementNode {

    private final ExpressionNode expression;

    public ThrowStatementNode(ExpressionNode expression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ThrowStatementNode{" +
               "expression=" + expression +
               '}';
    }
}
