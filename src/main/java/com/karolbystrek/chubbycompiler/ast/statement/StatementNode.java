package com.karolbystrek.chubbycompiler.ast.statement;

import com.karolbystrek.chubbycompiler.ast.AstNode;

public abstract class StatementNode extends AstNode {

    public StatementNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }
}
