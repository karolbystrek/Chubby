package com.karolbystrek.chubbycompiler.ast.expression.literal;


import java.util.Objects;


public class BooleanLiteralNode extends LiteralNode<Boolean> {
    public BooleanLiteralNode(Boolean value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }
}