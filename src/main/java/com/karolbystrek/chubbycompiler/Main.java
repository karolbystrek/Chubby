package com.karolbystrek.chubbycompiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide the path to the Chubby source file.");
            return;
        }
        File chubbyFile = new File(args[0]);
        CharStream fileInputStream;
        try (FileReader fileReader = new FileReader(chubbyFile)) {
            fileInputStream = CharStreams.fromReader(fileReader);
        } catch (Exception e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }
        CharStream input = CharStreams.fromString(fileInputStream.toString());

        ChubbyParser parser = getChubbyParser(input);

        try {
            ParseTree tree = parser.program();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                System.out.println("Parsing failed with errors.");
            } else {
                System.out.println("Parsing successful. No syntax errors found.");
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
