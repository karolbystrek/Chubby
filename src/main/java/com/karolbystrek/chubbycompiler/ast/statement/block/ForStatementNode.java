package com.karolbystrek.chubbycompiler.ast.statement.block;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class ForStatementNode extends StatementNode {

    private final StatementNode initialization;
    private final ExpressionNode condition;
    private final StatementNode update;
    private final BlockNode body;

    public ForStatementNode(StatementNode initialization,
                            ExpressionNode condition,
                            StatementNode update,
                            BlockNode body,
                            int lineNumber,
                            int columnNumber) {
        super(lineNumber, columnNumber);
        // Initialization, condition, and update can be null according to grammar
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
