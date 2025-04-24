package com.karolbystrek.chubbycompiler.ast.statement.simple;

import com.karolbystrek.chubbycompiler.ast.AstNode;

public class LValueNode extends AstNode {

    private final String name;

    public LValueNode(String name, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LValueNode{" +
                "name='" + name + '\'' +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }
}
