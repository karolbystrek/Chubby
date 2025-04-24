package com.karolbystrek.chubbycompiler.ast.expression.literal;


import java.util.Objects;


public class CharLiteralNode extends LiteralNode<Character> {
    public CharLiteralNode(Character value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("%s['%s' @%d:%d]",
                getClass().getSimpleName().replace("LiteralNode", "Lit"),
                getValue(), getLineNumber(), getColumnNumber());
    }
}