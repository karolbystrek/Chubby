package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;

/**
 * Represents an expression that can appear on the left-hand side of an assignment.
 * Examples include variables, array elements, object fields.
 */
public abstract class LValueNode extends ExpressionNode {

    public LValueNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }
}
