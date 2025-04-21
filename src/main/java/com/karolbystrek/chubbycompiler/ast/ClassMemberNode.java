package com.karolbystrek.chubbycompiler.ast;

public abstract class ClassMemberNode extends AstNode {

    public ClassMemberNode(int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
    }

    abstract Visibility getVisibility();

    abstract String getName();
}
