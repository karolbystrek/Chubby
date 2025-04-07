package com.karolbystrek.chubbycompiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    public static void main(String[] args) {
        String chubbyCode = """
                import SomePackage.Entity;
                
                # Main application class containing the entry point and utility methods
                class public ExampleApp
                
                    # Entry point method for the application
                    # Assumed to be static for execution like Java's main
                    function public static main() : void
                        # Test integer and float declarations
                        int integerVar = 42;
                        float floatVar = 3.14;
                        double doubleVar = 2.718;
                
                        # Test string and boolean declarations
                        string message = "Hello, World!";
                        bool isTrue = true;
                        bool isFalse = false;
                
                        # Test arithmetic operations
                        int a = 10;
                        int b = 20;
                        # Calls to static methods within the same class
                        int sum = add(a, b);
                        int difference = b - a;
                        int product = multiply(a, b);
                        int quotient = divide(b, a);
                        int modulo = b % a;
                
                        # Test compound assignment operators
                        int x = 5;
                        x += 3;   # x is now 8
                        x -= 2;   # x is now 6
                        x *= 2;   # x is now 12
                        x /= 3;   # x is now 4
                        x %= 2;   # x is now 0
                
                        # Test comparison and logical operators
                        if (a > b and a < b) then
                            print("Complex condition is true");
                        elsif (a > b) then
                            print("a>b");
                        endif
                
                        if (a == b or a != b) then
                            print("Equality and inequality test");
                        endif
                
                        # Test nested conditionals
                        if (integerVar > 40) then
                            if (floatVar < 4.0) then
                                print("Nested condition met");
                            else
                                print("Outer condition true, inner false");
                            endif
                        endif
                
                        # Test different types of loops
                        for (int i = 0; i < 3; i += 1) then
                            while (i < 2) then
                                print("Nested loop: " + i);
                                break;
                            endwhile
                        endfor
                
                        int result1 = add(a, b);
                        int result2 = multiply(a, b);
                        print("Addition result: " + result1);
                        print("Multiplication result: " + result2);
                
                        int[] singleArray = new int[5];
                        singleArray[0] = 10;
                        singleArray[1] = 20;
                        print("Single array element 0: " + singleArray[0]);
                
                        int[][] multiArray = new int[3][3];
                        multiArray[0][0] = 1;
                        multiArray[1][1] = 2;
                        print("Multi array element [1][1]: " + multiArray[1][1]);
                
                        try
                            print("Attempting division by zero...");
                            int error = divide(10, 0); # Calling the static method
                            print("This should not be printed.");
                        catch (Exception e)
                            print("Division by zero caught!");
                        endtry
                       \s
                        Person person = new Person("John", 30);
                        person.get(); # Calling an instance method on the Person object
                
                        print("Execution finished.");
                    endfunction # End of main method
                
                    function public static add(int x, int y) : int
                        return x + y;
                    endfunction
                
                    # Utility function for multiplication (now a static method of ExampleApp)
                    function public static multiply(int x, int y) : int
                        return x * y;
                    endfunction
                
                    # Utility function for division (now a static method of ExampleApp)
                    # Intentionally allows division by zero to test try-catch
                    function public static divide(int x, int y) : int
                        return x / y;
                    endfunction
                
                endclass # End of ExampleApp class
                
                
                # Separate class definition for Person
                class public Person implements Entity
                    public string name;
                    public int age;
                
                    # Constructor for Person class
                    constructor Person(string name, int age)
                        this.name = name;
                        this.age = age;
                    endconstructor
                
                    # Instance method for Person class
                    function public get() : void
                        print("Hello, my name is " + name + " and I am " + age + " years old");
                    endfunction
                
                endclass # End of Person class
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
