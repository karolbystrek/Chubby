package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

/**
 * Represents a break statement.
 */
public class BreakStatementNode extends StatementNode {

    public BreakStatementNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return "BreakStatementNode{" +
                "lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }
}
