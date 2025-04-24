package com.karolbystrek.chubbycompiler.ast.expression.literal;

import java.util.Objects;


public class IntegerLiteralNode extends LiteralNode<Integer> {
    public IntegerLiteralNode(Integer value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }
}