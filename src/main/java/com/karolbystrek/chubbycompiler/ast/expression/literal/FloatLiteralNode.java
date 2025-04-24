package com.karolbystrek.chubbycompiler.ast.expression.literal;


import java.util.Objects;

public class FloatLiteralNode extends LiteralNode<Float> {
    public FloatLiteralNode(Float value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }
}