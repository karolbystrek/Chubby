package com.karolbystrek.chubbycompiler.ast;

import java.util.Objects;

public class TypeNode extends AstNode {

    private final String baseTypeName;
    private final int arrayDimensions; // 0 = not an array, 1 = [], 2 = [][] etc.

    public TypeNode(String baseTypeName, int lineNumber, int columnNumber) {
        this(baseTypeName, 0, lineNumber, columnNumber);
    }

    public TypeNode(String baseTypeName, int arrayDimensions, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.baseTypeName = baseTypeName;
        this.arrayDimensions = arrayDimensions;
    }

    public String getBaseTypeName() {
        return baseTypeName;
    }

    public int getArrayDimensions() {
        return arrayDimensions;
    }

    public boolean isArray() {
        return arrayDimensions > 0;
    }

    public boolean isVoid() {
        return "void".equals(baseTypeName) && arrayDimensions == 0;
    }

    @Override
    public String toString() {
        return "TypeNode{" +
                "baseTypeName='" + baseTypeName + '\'' +
                ", arrayDimensions=" + arrayDimensions +
                ", lineNumber=" + super.getLineNumber() +
                ", columnNumber=" + super.getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeNode typeNode = (TypeNode) o;
        return arrayDimensions == typeNode.arrayDimensions &&
                Objects.equals(baseTypeName, typeNode.baseTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseTypeName, arrayDimensions);
    }
}
