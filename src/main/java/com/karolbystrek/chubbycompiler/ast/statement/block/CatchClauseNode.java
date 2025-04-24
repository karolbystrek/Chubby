package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.List;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import com.karolbystrek.chubbycompiler.ast.ParameterNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class CatchClauseNode extends AstNode {

    private final ParameterNode exceptionParameter;
    private final List<StatementNode> body;

    public CatchClauseNode(ParameterNode exceptionParameter,
                           List<StatementNode> body,
                           int lineNumber,
                           int columnNumber) {
        super(lineNumber, columnNumber);
        this.exceptionParameter = exceptionParameter;
        this.body = body;
    }

    public ParameterNode getExceptionParameter() {
        return exceptionParameter;
    }

    public List<StatementNode> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "CatchClauseNode{" +
                "exceptionParameter=" + exceptionParameter +
                ", body=" + body +
                '}';
    }
}
