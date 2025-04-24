package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.List;
import java.util.Map;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class IfStatementNode extends StatementNode {

    private final ExpressionNode condition;
    private final List<StatementNode> thenBranch;
    private final List<Map.Entry<ExpressionNode, List<StatementNode>>> elsifBranches;
    private final List<StatementNode> elseBranch;

    public IfStatementNode(ExpressionNode condition,
                           List<StatementNode> thenBranch,
                           List<Map.Entry<ExpressionNode, List<StatementNode>>> elsifBranches,
                           List<StatementNode> elseBranch,
                           int lineNumber,
                           int columnNumber) {
        super(lineNumber, columnNumber);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elsifBranches = elsifBranches;
        this.elseBranch = elseBranch;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public List<StatementNode> getThenBranch() {
        return thenBranch;
    }

    public List<Map.Entry<ExpressionNode, List<StatementNode>>> getElsifBranches() {
        return elsifBranches;
    }

    public List<StatementNode> getElseBranch() {
        return elseBranch;
    }

    @Override
    public String toString() {
        return "IfStatementNode{" +
                "condition=" + condition +
                ", thenBranch=" + thenBranch +
                ", elsifBranches=" + elsifBranches +
                ", elseBranch=" + elseBranch +
                '}';
    }
}
