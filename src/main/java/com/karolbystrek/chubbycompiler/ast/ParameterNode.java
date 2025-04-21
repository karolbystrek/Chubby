package com.karolbystrek.chubbycompiler.ast;

public class ParameterNode extends AstNode {

    private final TypeNode type;
    private final String name;

    public ParameterNode(TypeNode type, String name, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.type = type;
        this.name = name;
    }

    public TypeNode getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ParameterNode{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
