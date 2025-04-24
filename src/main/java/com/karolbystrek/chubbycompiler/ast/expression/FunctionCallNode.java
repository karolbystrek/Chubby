package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a function call, potentially as part of a postfix expression (e.g., obj.method(args) or func(args)).
 * The 'function' being called might be an IdentifierNode or a MemberAccessNode.
 */
public class FunctionCallNode extends ExpressionNode {

    private final ExpressionNode functionExpression;
    private final List<ExpressionNode> arguments;

    public FunctionCallNode(ExpressionNode functionExpression, List<ExpressionNode> arguments, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.functionExpression = Objects.requireNonNull(functionExpression, "Function expression cannot be null");
        this.arguments = arguments == null ? Collections.emptyList() : Collections.unmodifiableList(arguments);
    }

    public ExpressionNode getFunctionExpression() {
        return functionExpression;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "FunctionCallNode{" +
                "function=" + functionExpression +
                ", args=" + arguments +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionCallNode that = (FunctionCallNode) o;
        return Objects.equals(functionExpression, that.functionExpression) && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionExpression, arguments);
    }
}
