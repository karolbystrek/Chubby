package com.karolbystrek.chubbycompiler.ast.statement.block;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

import java.util.Objects;

public class WhileStatementNode extends StatementNode {

    private final ExpressionNode condition;
    private final BlockNode body;

    public WhileStatementNode(ExpressionNode condition,
                              BlockNode body,
                              int lineNumber,
                              int columnNumber) {
        super(lineNumber, columnNumber);
        this.condition = Objects.requireNonNull(condition, "While condition cannot be null");
        this.body = Objects.requireNonNull(body, "While body cannot be null");
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WhileStatementNode{" +
                "condition=" + condition +
                ", body=" + body +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhileStatementNode that = (WhileStatementNode) o;
        return Objects.equals(condition, that.condition) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, body);
    }
}
