package com.karolbystrek.chubbycompiler.ast.statement.block;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class ForStatementNode extends StatementNode {

    private final StatementNode initialization;
    private final ExpressionNode condition;
    private final StatementNode update;
    private final StatementNode body;

    public ForStatementNode(StatementNode initialization,
                            ExpressionNode condition,
                            StatementNode update,
                            StatementNode body,
                            int lineNumber,
                            int columnNumber) {
        super(lineNumber, columnNumber);
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    public StatementNode getInitialization() {
        return initialization;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public StatementNode getUpdate() {
        return update;
    }

    public StatementNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ForStatementNode{" +
                "initialization=" + initialization +
                ", condition=" + condition +
                ", update=" + update +
                ", body=" + body +
                '}';
    }
}
