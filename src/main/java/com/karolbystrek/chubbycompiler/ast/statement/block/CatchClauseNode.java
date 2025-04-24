package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.Objects;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import com.karolbystrek.chubbycompiler.ast.ParameterNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;

public class CatchClauseNode extends AstNode {

    private final ParameterNode exceptionParameter;
    private final BlockNode body;

    public CatchClauseNode(ParameterNode exceptionParameter,
                           BlockNode body,
                           int lineNumber,
                           int columnNumber) {
        super(lineNumber, columnNumber);
        this.exceptionParameter = Objects.requireNonNull(exceptionParameter, "Catch exception parameter cannot be null");
        this.body = Objects.requireNonNull(body, "Catch body cannot be null");
    }

    public ParameterNode getExceptionParameter() {
        return exceptionParameter;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "CatchClauseNode{" +
                "exceptionParameter=" + exceptionParameter +
                ", body=" + body +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatchClauseNode that = (CatchClauseNode) o;
        return Objects.equals(exceptionParameter, that.exceptionParameter) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exceptionParameter, body);
    }
}
