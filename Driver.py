import sys
from antlr4 import *
from antlr.ChubbyParser import ChubbyParser
from antlr.ChubbyLexer import ChubbyLexer
from src.ChubbyCompiler import ChubbyCompiler, ChubbyCompilerError, ChubbyErrorListener


def main(argv):
    if len(argv) < 2:
        print("Usage: python Driver.py <input_file>")
        sys.exit(1)

    stream = FileStream(argv[1])
    lexer = ChubbyLexer(stream)
    stream = CommonTokenStream(lexer)
    parser = ChubbyParser(stream)

    if parser.getNumberOfSyntaxErrors() > 0:
        error_listener = ChubbyErrorListener()
        parser.removeErrorListeners()
        parser.addErrorListener(error_listener)
        parser.program()

    program = parser.program()
    compiler = ChubbyCompiler()
    try:
        compiler.compile(program)
    except ChubbyCompilerError as e:
        print(f"Compilation error: {e}")


if __name__ == "__main__":
    main(sys.argv)
