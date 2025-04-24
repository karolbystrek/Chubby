package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

import java.util.Objects;

/**
 * Represents a statement that consists solely of an expression
 * (e.g., a function call, increment/decrement).
 */
public class ExpressionStatementNode extends StatementNode {

    private final ExpressionNode expression;

    public ExpressionStatementNode(ExpressionNode expression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.expression = Objects.requireNonNull(expression, "Expression cannot be null");
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ExpressionStatementNode{" +
                "expression=" + expression +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionStatementNode that = (ExpressionStatementNode) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
