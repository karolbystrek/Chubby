package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.TypeNode; //

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ObjectCreationNode extends ExpressionNode {

    private final TypeNode typeToCreate;
    private final List<ExpressionNode> arguments;


    public ObjectCreationNode(TypeNode typeToCreate, List<ExpressionNode> arguments, int newLine, int newColumn) {
        super(newLine, newColumn);
        Objects.requireNonNull(typeToCreate, "Type to create cannot be null");
        if (typeToCreate.isArray()) { //
            throw new IllegalArgumentException("ObjectCreationNode requires a non-array type.");
        }
        this.typeToCreate = typeToCreate;
        this.arguments = arguments == null ? Collections.emptyList() : Collections.unmodifiableList(arguments);
    }

    public TypeNode getTypeToCreate() {
        return typeToCreate;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        String argsStr = arguments.stream().map(Objects::toString).collect(Collectors.joining(", "));
        return String.format("NewObject[type=%s, args=(%s) @%d:%d]",
                typeToCreate.getBaseTypeName(), argsStr, getLineNumber(), getColumnNumber()); //
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectCreationNode that = (ObjectCreationNode) o;
        return typeToCreate.equals(that.typeToCreate) && arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeToCreate, arguments);
    }
}