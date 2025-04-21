package com.karolbystrek.chubbycompiler.ast;

import java.util.List;

public class FunctionDefinitionNode extends ClassMemberNode {

    private final Visibility visibility;
    private final boolean isStatic;
    private final String name;
    private final List<ParameterNode> parameters;
    private final TypeNode returnType;
    private final List<AstNode> body;

    public FunctionDefinitionNode(Visibility visibility,
                                  boolean isStatic,
                                  String name,
                                  List<ParameterNode> parameters,
                                  TypeNode returnType,
                                  List<AstNode> body,
                                  int lineNumber,
                                  int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public List<AstNode> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "FunctionDefinitionNode{" +
                "visibility=" + visibility +
                ", isStatic=" + isStatic +
                ", name='" + name + '\'' +
                ", parameters=" + parameters +
                ", returnType=" + returnType +
                ", body=" + body +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
