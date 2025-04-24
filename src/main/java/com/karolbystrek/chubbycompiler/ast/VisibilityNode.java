package com.karolbystrek.chubbycompiler.ast;

public class VisibilityNode extends AstNode {
    private final Visibility visibility;

    public VisibilityNode(Visibility visibility, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = visibility;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String toString() {
        return "VisibilityNode{" +
                "visibility=" + visibility +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
