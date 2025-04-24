package com.karolbystrek.chubbycompiler.ast;

public class VariableDefinitionNode extends ClassMemberNode {

    private final Visibility visibility;
    private final TypeNode type;
    private final String name;
    private final boolean isStatic;
    private final boolean isConst;
    private final AstNode initializer;

    public VariableDefinitionNode(Visibility visibility,
                                  TypeNode type,
                                  String name,
                                  boolean isStatic,
                                  boolean isConst,
                                  AstNode initializer,
                                  int lineNumber,
                                  int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = visibility;
        this.type = type;
        this.name = name;
        this.isStatic = isStatic;
        this.isConst = isConst;
        this.initializer = initializer;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String getName() {
        return name;
    }

    public TypeNode getType() {
        return type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isConst() {
        return isConst;
    }

    public AstNode getInitializer() {
        return initializer;
    }

    @Override
    public String toString() {
        return "VariableDefinitionNode{" +
                "visibility=" + visibility +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", isStatic=" + isStatic +
                ", isConst=" + isConst +
                ", initializer=" + initializer +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
