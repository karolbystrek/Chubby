package com.karolbystrek.chubbycompiler;

public class VisibilityModifierNode extends AstNode {

    private final Visibility visibility;

    public VisibilityModifierNode(Visibility visibility, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.visibility = visibility;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String toString() {
        return "VisibilityModifierNode{" +
                "visibility='" + visibility + '\'' +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
