package com.karolbystrek.chubbycompiler.ast.expression.literal;

import java.util.Objects;


public class DoubleLiteralNode extends LiteralNode<Double> {
    public DoubleLiteralNode(Double value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }
}