package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class BreakStatementNode extends StatementNode {

    public BreakStatementNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return "BreakStatementNode{}";
    }
}
