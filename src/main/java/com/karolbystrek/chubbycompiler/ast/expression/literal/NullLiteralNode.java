package com.karolbystrek.chubbycompiler.ast.expression.literal;


public class NullLiteralNode extends LiteralNode<Object> {
    public NullLiteralNode(int lineNumber, int columnNumber) {
        super(null, lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("%s[null @%d:%d]",
                getClass().getSimpleName().replace("LiteralNode", "Lit"),
                getLineNumber(), getColumnNumber());
    }
}