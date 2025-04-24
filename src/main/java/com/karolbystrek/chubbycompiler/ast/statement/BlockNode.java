package com.karolbystrek.chubbycompiler.ast.statement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a block of statements, typically enclosed in braces or
 * forming the body of control structures (if, loops, functions, etc.).
 * This node itself is considered a statement.
 */
public class BlockNode extends StatementNode {

    private final List<StatementNode> statements;

    public BlockNode(List<StatementNode> statements, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.statements = statements == null ? Collections.emptyList() : Collections.unmodifiableList(statements);
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "BlockNode{" +
                "statements=" + statements +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockNode blockNode = (BlockNode) o;
        return Objects.equals(statements, blockNode.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }
}
