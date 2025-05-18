// filepath: src/main/java/com/karolbystrek/chubbycompiler/symbol/Scope.java
package com.karolbystrek.chubbycompiler.symbol;

import com.karolbystrek.chubbycompiler.exceptions.CompilationException;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Scope parent;
    // TODO: Add scope type (e.g., GLOBAL, CLASS, METHOD, BLOCK) if needed for specific rules

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope getParent() {
        return parent;
    }


    public void addSymbol(Symbol symbol) throws CompilationException {
        if (symbols.containsKey(symbol.getName())) {
            throw new CompilationException("Symbol '" + symbol.getName() + "' already defined in this scope.",
                    symbol.getLineNumber(), symbol.getColumnNumber());
        }
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol resolve(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        }
        if (parent != null) {
            return parent.resolve(name);
        }
        return null; // Symbol nie znaleziony
    }

    // TODO: Add a method to resolve symbols only in the current scope (for checking re-declaration)
    // TODO: Add a method to get all symbols in the current scope or all accessible symbols
}