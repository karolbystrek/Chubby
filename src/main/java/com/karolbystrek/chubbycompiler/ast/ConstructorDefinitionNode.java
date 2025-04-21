package com.karolbystrek.chubbycompiler.ast;

import java.util.List;

public class ConstructorDefinitionNode extends ClassMemberNode {

    private final Visibility visibility = Visibility.PUBLIC;
    private final String name;
    private final List<AstNode> parameters;
    private final List<AstNode> body;

    public ConstructorDefinitionNode(String name,
                                     List<AstNode> parameters,
                                     List<AstNode> body,
                                     int lineNumber,
                                     int columnNumber) {
        super(lineNumber, columnNumber);
        this.name = name;
        this.parameters = parameters;
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

    public List<AstNode> getParameters() {
        return parameters;
    }

    public List<AstNode> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ConstructorDefinitionNode{" +
                "visibility=" + visibility +
                ", name='" + name + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
