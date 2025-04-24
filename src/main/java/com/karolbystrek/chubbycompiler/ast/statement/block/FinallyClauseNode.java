package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.List;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class FinallyClauseNode extends AstNode {

    private final List<StatementNode> body;

    public FinallyClauseNode(List<StatementNode> body, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.body = body;
    }

    public List<StatementNode> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "FinnallyClauseNode{" +
                "body=" + body +
                '}';
    }
}
