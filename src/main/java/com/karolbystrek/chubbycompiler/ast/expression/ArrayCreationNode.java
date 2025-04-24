package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.TypeNode; //

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ArrayCreationNode extends ExpressionNode {

    private final TypeNode elementType;
    private final List<ExpressionNode> dimensionSizes;


    public ArrayCreationNode(TypeNode elementType, List<ExpressionNode> dimensionSizes, int newLine, int newColumn) {
        super(newLine, newColumn);
        this.elementType = Objects.requireNonNull(elementType, "Element type cannot be null");
        if (dimensionSizes == null || dimensionSizes.isEmpty()) {
            throw new IllegalArgumentException("Array creation requires at least one dimension size expression.");
        }

        for(ExpressionNode size : dimensionSizes) {
            Objects.requireNonNull(size, "Dimension size expression cannot be null");
        }
        this.dimensionSizes = Collections.unmodifiableList(dimensionSizes);
    }

    public TypeNode getElementType() {
        return elementType;
    }

    public List<ExpressionNode> getDimensionSizes() {
        return dimensionSizes;
    }

    @Override
    public String toString() {
        String dimsStr = dimensionSizes.stream().map(Objects::toString).collect(Collectors.joining("]["));
        return String.format("NewArray[type=%s, dims=[%s] @%d:%d]",
                elementType.getBaseTypeName(), dimsStr, getLineNumber(), getColumnNumber()); //
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayCreationNode that = (ArrayCreationNode) o;
        return elementType.equals(that.elementType) && dimensionSizes.equals(that.dimensionSizes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementType, dimensionSizes);
    }
}