package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import com.karolbystrek.chubbycompiler.ast.TypeNode;

public abstract class ExpressionNode extends AstNode {

    private TypeNode analyzedType;
    public ExpressionNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    public void setAnalyzedType(TypeNode analyzedType) {
        this.analyzedType = analyzedType;
    }

    public TypeNode getAnalyzedType() {
        return analyzedType;
    }
}