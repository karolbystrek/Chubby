package com.karolbystrek.chubbycompiler.ast.expression.literal;

import java.util.Objects;


public class StringLiteralNode extends LiteralNode<String> {
    public StringLiteralNode(String value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("%s[\"%s\" @%d:%d]",
                getClass().getSimpleName().replace("LiteralNode", "Lit"),
                getValue(), getLineNumber(), getColumnNumber());
    }
}