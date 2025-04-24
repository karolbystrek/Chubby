package com.karolbystrek.chubbycompiler.ast;

import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ProgramNode extends AstNode {


    private final List<StatementNode> statements;


    public ProgramNode(List<StatementNode> statements, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.statements = Collections.unmodifiableList(
                Objects.requireNonNull(statements, "Statements list cannot be null")
        );
    }


    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "statements=" + statements.stream()
                .map(StatementNode::toString)
                .collect(Collectors.joining(", ")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        ProgramNode that = (ProgramNode) o;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statements);
    }
}
