package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.TypeNode;
import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class LocalVariableDeclarationNode extends StatementNode {

    private final TypeNode typeSpecifier;
    private final String variableName;
    private final ExpressionNode initializer;

    public LocalVariableDeclarationNode(TypeNode typeSpecifier,
                                         String variableName,
                                         ExpressionNode initializer,
                                         int lineNumber,
                                         int columnNumber) {
        super(lineNumber, columnNumber);
        this.typeSpecifier = typeSpecifier;
        this.variableName = variableName;
        this.initializer = initializer;
    }

    public TypeNode getTypeSpecifier() {
        return typeSpecifier;
    }


    public String getVariableName() {
        return variableName;
    }

    public ExpressionNode getInitializer() {
        return initializer;
    }

    @Override
    public String toString() {
        return "LocalVariableDeclarationNode{" +
                "typeSpecifier=" + typeSpecifier +
                ", variableName='" + variableName + '\'' +
                ", initializer=" + initializer +
                '}';
    }
}
