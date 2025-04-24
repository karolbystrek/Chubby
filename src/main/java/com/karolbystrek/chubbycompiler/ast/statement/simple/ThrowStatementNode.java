package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

import java.util.Objects;

/**
 * Represents a throw statement.
 */
public class ThrowStatementNode extends StatementNode {

    private final ExpressionNode expression;

    public ThrowStatementNode(ExpressionNode expression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.expression = Objects.requireNonNull(expression, "Expression in throw statement cannot be null");
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ThrowStatementNode{" +
                "expression=" + expression +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThrowStatementNode that = (ThrowStatementNode) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
