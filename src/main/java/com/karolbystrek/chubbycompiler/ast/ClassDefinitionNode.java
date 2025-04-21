package com.karolbystrek.chubbycompiler.ast;

import java.util.Collections;
import java.util.List;

public class ClassDefinitionNode extends AstNode {
    private final Visibility visibility;
    private final String name;
    private final List<AstNode> members;

    public ClassDefinitionNode(Visibility visibility,
                               String name,
                               List<AstNode> members,
                               int lineNumber,
                               int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = visibility;
        this.name = name;
        this.members = Collections.unmodifiableList(
                members != null ? members : Collections.emptyList()
        );
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getName() {
        return name;
    }

    public List<AstNode> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "ClassDefinitionNode{" +
                "visibility=" + visibility +
                ", name='" + name + '\'' +
                ", members=" + members +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
