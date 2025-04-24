package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.Objects;

import com.karolbystrek.chubbycompiler.ast.AstNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;

public class FinallyClauseNode extends AstNode {

    private final BlockNode body;

    public FinallyClauseNode(BlockNode body, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.body = Objects.requireNonNull(body, "Finally body cannot be null");
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "FinallyClauseNode{" +
                "body=" + body +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinallyClauseNode that = (FinallyClauseNode) o;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
