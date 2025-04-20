package com.karolbystrek.chubbycompiler;

public abstract class ClassMemberNode extends AstNode {

    public ClassMemberNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    abstract Visibility getVisibility();

    abstract String getName();
}
