package com.karolbystrek.chubbycompiler.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class ProgramNode extends AstNode {

    private final List<ImportStatementNode> imports;
    private final List<ClassDefinitionNode> classDefinitions;


    public ProgramNode(List<ImportStatementNode> imports, List<ClassDefinitionNode> classDefinitions, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.imports = Collections.unmodifiableList(
                Objects.requireNonNull(imports, "Imports list cannot be null")
        );
        this.classDefinitions = Collections.unmodifiableList(
                Objects.requireNonNull(classDefinitions, "Class definitions list cannot be null")
        );
    }

    public List<ImportStatementNode> getImports() {
        return imports;
    }

    public List<ClassDefinitionNode> getClassDefinitions() {
        return classDefinitions;
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "imports=" + imports +
                ", classDefinitions=" + classDefinitions +
                ", lineNumber=" + getLineNumber() +
                ", columnNumber=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramNode that = (ProgramNode) o;
        return Objects.equals(imports, that.imports) &&
               Objects.equals(classDefinitions, that.classDefinitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imports, classDefinitions);
    }
}
