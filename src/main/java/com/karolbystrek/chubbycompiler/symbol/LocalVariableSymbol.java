package com.karolbystrek.chubbycompiler.symbol;

import com.karolbystrek.chubbycompiler.ast.TypeNode;

public class LocalVariableSymbol extends Symbol {
    public LocalVariableSymbol(String name, TypeNode type, Scope scope, int lineNumber, int columnNumber) {
        super(name, type, scope, lineNumber, columnNumber);
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.LOCAL_VARIABLE;
    }
}