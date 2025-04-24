package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.List;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class WhileStatementNode extends StatementNode {

    private final ExpressionNode condition;
    private final List<StatementNode> body;

    public WhileStatementNode(ExpressionNode condition,
                              List<StatementNode> body,
                              int lineNumber,
                              int columnNumber) {
        super(lineNumber, columnNumber);
        this.condition = condition;
        this.body = body;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public List<StatementNode> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WhileStatementNode{" +
                "condition=" + condition +
                ", body=" + body +
                '}';
    }
}
