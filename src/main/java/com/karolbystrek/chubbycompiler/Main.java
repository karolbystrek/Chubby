package com.karolbystrek.chubbycompiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    public static void main(String[] args) {
        String chubbyCode = """
                class public MinimalApp
                    # A minimal main function
                    function public main() : void
                        int testVar = 100;
                        testVar = testVar + 5;
                        print("Minimal Chubby Code Executed: " + testVar);
                    endfunction
                endclass
                """;
        CharStream input = CharStreams.fromString(chubbyCode);

        ChubbyParser parser = getChubbyParser(input);

        try {
            ParseTree tree = parser.program();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                System.out.println("Parsing failed with errors.");
            } else {
                System.out.println("Parsing successful. No syntax errors found.");
                System.out.println(tree.toStringTree(parser));
            }
        } catch (RecognitionException e) {
            System.err.println("Recognition error: " + e.getMessage());
        }
    }

    private static ChubbyParser getChubbyParser(CharStream input) {
        ChubbyLexer lexer = new ChubbyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ChubbyParser parser = new ChubbyParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                System.err.println("Syntax error at line " + line + ":" + charPositionInLine + " - " + msg);
            }
        });
        return parser;
    }
}
