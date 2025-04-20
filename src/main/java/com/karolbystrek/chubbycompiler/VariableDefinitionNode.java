package com.karolbystrek.chubbycompiler;

public class VariableDefinitionNode extends ClassMemberNode {

    private final Visibility visibility;
    private final String type;
    private final String name;
    private final boolean isStatic;
    private final boolean isFinal;
    private final AstNode initializer;

    public VariableDefinitionNode(Visibility visibility,
                                  String type,
                                  String name,
                                  boolean isStatic,
                                  boolean isFinal,
                                  AstNode initializer,
                                  int lineNumber,
                                  int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = visibility;
        this.type = type;
        this.name = name;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
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

    public String getType() {
        return type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
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
                ", isFinal=" + isFinal +
                ", initializer=" + initializer +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
