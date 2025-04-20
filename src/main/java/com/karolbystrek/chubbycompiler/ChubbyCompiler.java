package com.karolbystrek.chubbycompiler;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class ChubbyCompiler {

    public void compiler(String sourceCode) {
        ChubbyLexer lexer = new ChubbyLexer(CharStreams.fromString(sourceCode));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ChubbyParser parser = new ChubbyParser(tokens);

        ChubbyParser.ProgramContext tree = parser.program();

        AstBuilderVisitor visitor = new AstBuilderVisitor();
        AstNode ast = visitor.visit(tree);

        // TODO: Add semantic analysis and code generation
    }
}
