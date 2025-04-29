package com.karolbystrek.chubbycompiler.ast.statement.block;

import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.expression.IdentifierNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class ForStatementNode extends StatementNode {

    private final IdentifierNode identifier;
    private final ExpressionNode startExpression;
    private final ExpressionNode endExpression;
    private final ExpressionNode stepExpression;
    private final BlockNode body;

    public ForStatementNode(IdentifierNode identifier,
                            ExpressionNode startExpression,
                            ExpressionNode endExpression,
                            ExpressionNode stepExpression,
                            BlockNode body,
                            int lineNumber,
                            int columnNumber) {
        super(lineNumber, columnNumber);
        this.identifier = identifier;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
        this.body = body;
    }

    public IdentifierNode getIdentifier() {
        return identifier;
    }

    public ExpressionNode getStartExpression() {
        return startExpression;
    }

    public ExpressionNode getEndExpression() {
        return endExpression;
    }

    public ExpressionNode getStepExpression() {
        return stepExpression;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ForStatementNode{" +
                "identifier=" + identifier +
                ", startExpression=" + startExpression +
                ", endExpression=" + endExpression +
                ", stepExpression=" + stepExpression +
                ", body=" + body +
                '}';
    }
}
