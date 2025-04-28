package com.karolbystrek.chubbycompiler.parser;

import org.antlr.v4.runtime.*;

public class DescriptiveErrorListener extends BaseErrorListener {
    public static final DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();

    private DescriptiveErrorListener() {}

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        String sourceName = recognizer.getInputStream().getSourceName();
        if (sourceName != null && !sourceName.isEmpty() && !sourceName.equals("<unknown>")) {
             sourceName = sourceName + ":";
        } else {
            sourceName = "";
        }

        String errorMessage = String.format("%sline %d:%d %s", sourceName, line, charPositionInLine, msg);
        System.err.println("Syntax Error: " + errorMessage);
    }
}
