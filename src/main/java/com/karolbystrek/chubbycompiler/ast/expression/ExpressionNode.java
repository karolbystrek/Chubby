package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.AstNode;

public abstract class ExpressionNode extends AstNode {

    public ExpressionNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }
}
