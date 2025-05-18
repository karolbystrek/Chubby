package com.karolbystrek.chubbycompiler.symbol;

import com.karolbystrek.chubbycompiler.ast.TypeNode;
import com.karolbystrek.chubbycompiler.ast.AstNode;

public abstract class Symbol extends AstNode {

    private final String name;
    private final TypeNode type;
    private final Scope scope;

    public Symbol(String name, TypeNode type, Scope scope, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public TypeNode getType() {
        return type;
    }

    public Scope getScope() {
        return scope;
    }

    public abstract SymbolKind getKind();

    @Override
    public String toString() {
        return String.format("%s[%s: %s]", getKind(), name, type != null ? type.getBaseTypeName() : "void");
    }


}