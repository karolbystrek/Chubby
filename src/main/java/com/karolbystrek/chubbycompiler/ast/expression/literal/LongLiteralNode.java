package com.karolbystrek.chubbycompiler.ast.expression.literal;

import java.util.Objects;

public class LongLiteralNode extends LiteralNode<Long> {
    public LongLiteralNode(Long value, int lineNumber, int columnNumber) {
        super(Objects.requireNonNull(value), lineNumber, columnNumber);
    }
}
