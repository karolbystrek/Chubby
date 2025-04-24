package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

import java.util.Objects;

/**
 * Represents a return statement, optionally with a return value.
 */
public class ReturnStatementNode extends StatementNode {

    private final ExpressionNode returnValue;

    public ReturnStatementNode(ExpressionNode returnValue, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.returnValue = returnValue;
    }

    public ExpressionNode getReturnValue() {
        return returnValue;
    }

    public boolean hasReturnValue() {
        return returnValue != null;
    }

    @Override
    public String toString() {
        return "ReturnStatementNode{" +
                "returnValue=" + returnValue +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnStatementNode that = (ReturnStatementNode) o;
        return Objects.equals(returnValue, that.returnValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnValue);
    }
}
