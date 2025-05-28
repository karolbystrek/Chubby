import sys
from src.ChubbyCompiler import ChubbyCompiler, ChubbyCompilerError


def main(argv):
    if len(argv) < 2:
        print("Usage: python Driver.py <input_file>")
        sys.exit(1)

    input_file = argv[1]
    with open(input_file, "r") as file:
        chubby_code = file.read()
    compiler = ChubbyCompiler()
    try:
        success, stout, sterr = compiler.compile(chubby_code)
        print("Compilation successful!" if success else "Compilation failed.")
        print("Standard Output:\n", stout, sep="")
        print("Standard Error:", sterr)
    except ChubbyCompilerError as e:
        print(f"Compilation error: {e}")


if __name__ == "__main__":
    main(sys.argv)
