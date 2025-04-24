package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class IfStatementNode extends StatementNode {

    public static class ElsifBranch extends AbstractMap.SimpleImmutableEntry<ExpressionNode, BlockNode> {
        public ElsifBranch(ExpressionNode condition, BlockNode body) {
            super(Objects.requireNonNull(condition, "Elsif condition cannot be null"),
                  Objects.requireNonNull(body, "Elsif body cannot be null"));
        }
        public ExpressionNode getCondition() { return getKey(); }
        public BlockNode getBody() { return getValue(); }
    }

    private final ExpressionNode condition;
    private final BlockNode thenBranch;
    private final List<ElsifBranch> elsifBranches;
    private final BlockNode elseBranch;

    public IfStatementNode(ExpressionNode condition,
                           BlockNode thenBranch,
                           List<ElsifBranch> elsifBranches,
                           BlockNode elseBranch,
                           int lineNumber,
                           int columnNumber) {
        super(lineNumber, columnNumber);
        this.condition = Objects.requireNonNull(condition, "If condition cannot be null");
        this.thenBranch = Objects.requireNonNull(thenBranch, "Then branch cannot be null");
        this.elsifBranches = elsifBranches == null ? Collections.emptyList() : Collections.unmodifiableList(elsifBranches);
        this.elseBranch = elseBranch;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getThenBranch() {
        return thenBranch;
    }

    public List<ElsifBranch> getElsifBranches() {
        return elsifBranches;
    }

    public BlockNode getElseBranch() {
        return elseBranch;
    }

    public boolean hasElseBranch() {
        return elseBranch != null;
    }

    @Override
    public String toString() {
        return "IfStatementNode{" +
                "condition=" + condition +
                ", thenBranch=" + thenBranch +
                ", elsifBranches=" + elsifBranches +
                ", elseBranch=" + elseBranch +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IfStatementNode that = (IfStatementNode) o;
        return Objects.equals(condition, that.condition) && Objects.equals(thenBranch, that.thenBranch) && Objects.equals(elsifBranches, that.elsifBranches) && Objects.equals(elseBranch, that.elseBranch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, thenBranch, elsifBranches, elseBranch);
    }
}
