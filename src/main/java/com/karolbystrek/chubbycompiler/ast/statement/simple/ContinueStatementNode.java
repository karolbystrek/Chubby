package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

/**
 * Represents a continue statement.
 */
public class ContinueStatementNode extends StatementNode {

    public ContinueStatementNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return "ContinueStatementNode{" +
                "lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }
}
