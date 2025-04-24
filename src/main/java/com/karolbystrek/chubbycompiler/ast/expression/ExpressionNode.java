package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import java.util.Objects;



public abstract class ExpressionNode extends AstNode {

    protected ExpressionNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass() && super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}