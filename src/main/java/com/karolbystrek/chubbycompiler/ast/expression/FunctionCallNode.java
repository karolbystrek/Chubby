package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FunctionCallNode extends ExpressionNode {

    private final ExpressionNode functionReference;
    private final List<ExpressionNode> arguments;

    public FunctionCallNode(ExpressionNode functionReference, List<ExpressionNode> arguments,
                            int callLine, int callColumn) {
        super(callLine, callColumn);
        this.functionReference = Objects.requireNonNull(functionReference, "Function reference cannot be null");
        this.arguments = arguments == null ? Collections.emptyList() : Collections.unmodifiableList(arguments);
    }

    public ExpressionNode getFunctionReference() {
        return functionReference;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        String argsStr = arguments.stream().map(Objects::toString).collect(Collectors.joining(", "));
        return String.format("FuncCall[ref=%s, args=(%s) @%d:%d]",
                functionReference, argsStr, getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionCallNode that = (FunctionCallNode) o;
        return functionReference.equals(that.functionReference) && arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionReference, arguments);
    }
}