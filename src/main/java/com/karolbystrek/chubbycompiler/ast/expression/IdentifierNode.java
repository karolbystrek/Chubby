package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.statement.simple.LValueNode;

import java.util.Objects;

/**
 * Represents an identifier used as an expression (e.g., variable access).
 * It can also function as an LValue.
 */
public class IdentifierNode extends LValueNode {

    private final String name;

    public IdentifierNode(String name, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.name = Objects.requireNonNull(name, "Identifier name cannot be null");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "IdentifierNode{" +
                "name='" + name + '\'' +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierNode that = (IdentifierNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
