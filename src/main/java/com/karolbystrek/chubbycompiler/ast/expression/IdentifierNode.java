package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.statement.simple.LValueNode;
import com.karolbystrek.chubbycompiler.symbol.Symbol;

import java.util.Objects;

public class IdentifierNode extends LValueNode {

    private final String name;
    private Symbol symbol;

    public IdentifierNode(String name, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.name = Objects.requireNonNull(name, "Identifier name cannot be null");
    }

    public String getName() {
        return name;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "IdentifierNode{" +
                "name='" + name + '\'' +
                (symbol != null ? ", resolvedSymbol=" + symbol.getName() : "") + // Pokaż nazwę symbolu
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