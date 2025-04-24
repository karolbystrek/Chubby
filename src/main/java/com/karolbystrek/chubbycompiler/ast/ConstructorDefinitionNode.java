package com.karolbystrek.chubbycompiler.ast;

import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

import java.util.List;
import java.util.Collections;

public class ConstructorDefinitionNode extends ClassMemberNode {

    private final Visibility visibility;
    private final String name;
    private final List<ParameterNode> parameters;
    private final List<StatementNode> body;

    public ConstructorDefinitionNode(Visibility visibility,
                                        String name,
                                        List<ParameterNode> parameters,
                                        List<StatementNode> body,
                                        int lineNumber,
                                        int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = (visibility != null) ? visibility : Visibility.PUBLIC;
        this.name = name;
        this.parameters = parameters == null ? Collections.emptyList() : Collections.unmodifiableList(parameters);
        this.body = body == null ? Collections.emptyList() : Collections.unmodifiableList(body);
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public List<StatementNode> getBody() {
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
