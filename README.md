# Chubby

Our own programming language made for TKiK university course.

---

## Authors

* **Karol Bystrek** - [karbystrek@student.agh.edu.pl](mailto:karbystrek@student.agh.edu.pl)
* **Patryk Chamera** - [pchamera@student.agh.edu.pl](mailto:pchamera@student.agh.edu.pl)

---

## Project Specification

### 1. General Goals

The main goal of the Chubby project is to design and implement a custom, object-oriented programming language as part of
the TKiK university course. This involves defining the language's syntax and semantics, developing a compiler, and
exploring concepts like lexical analysis, parsing, memory management, and error handling.

### 2. Translator Type

* **Compiler**

### 3. Planned Output

### 4. Implementation Language

* Chubby is implemented in **Java** programming language.

### 5. Scanner/Parser Implementation Method

---

## Token Description

| Token Type                         | Regular Expression Pattern | Description                            |
|:-----------------------------------|:---------------------------|:---------------------------------------|
| `keyword_CLASS`                    | `class`                    | Keyword 'class'                        |
| `keyword_ENDCLASS`                 | `endclass`                 | Keyword 'endclass'                     |
| `keyword_FUNCTION`                 | `function`                 | Keyword 'function'                     |
| `keyword_ENDFUNCTION`              | `endfunction`              | Keyword 'endfunction'                  |
| `keyword_CONSTRUCTOR`              | `constructor`              | Keyword 'constructor'                  |
| `keyword_ENDCONSTRUCTOR`           | `endconstructor`           | Keyword 'endconstructor'               |
| `keyword_IF`                       | `if`                       | Keyword 'if'                           |
| `keyword_THEN`                     | `then`                     | Keyword 'then'                         |
| `keyword_ELSIF`                    | `elsif`                    | Keyword 'elsif'                        |
| `keyword_ELSE`                     | `else`                     | Keyword 'else'                         |
| `keyword_ENDIF`                    | `endif`                    | Keyword 'endif'                        |
| `keyword_FOR`                      | `for`                      | Keyword 'for'                          |
| `keyword_ENDFOR`                   | `endfor`                   | Keyword 'endfor'                       |
| `keyword_WHILE`                    | `while`                    | Keyword 'while'                        |
| `keyword_ENDWHILE`                 | `endwhile`                 | Keyword 'endwhile'                     |
| `keyword_TRY`                      | `try`                      | Keyword 'try'                          |
| `keyword_CATCH`                    | `catch`                    | Keyword 'catch'                        |
| `keyword_FINALLY`                  | `finally`                  | Keyword 'finally'                      |
| `keyword_ENDTRY`                   | `endtry`                   | Keyword 'endtry'                       |
| `keyword_THROW`                    | `throw`                    | Keyword 'throw'                        |
| `keyword_RETURN`                   | `return`                   | Keyword 'return'                       |
| `keyword_EXTENDS`                  | `extends`                  | Keyword 'extends'                      |
| `keyword_IMPLEMENTS`               | `implements`               | Keyword 'implements'                   |
| `keyword_PUBLIC`                   | `public`                   | Keyword 'public'                       |
| `keyword_PROTECTED`                | `protected`                | Keyword 'protected'                    |
| `keyword_PRIVATE`                  | `private`                  | Keyword 'private'                      |
| `keyword_CONST`                    | `const`                    | Keyword 'const'                        |
| `keyword_OVERRIDE`                 | `override`                 | Keyword 'override'                     |
| `keyword_STATIC`                   | `static`                   | Keyword 'static'                       |
| `keyword_IMPORT`                   | `import`                   | Keyword 'import'                       |
| `keyword_NEW`                      | `new`                      | Keyword 'new'                          |
| `keyword_THIS`                     | `this`                     | Keyword 'this'                         |
| `keyword_VOID`                     | `void`                     | Keyword 'void'                         |
| `keyword_NULL`                     | `null`                     | Keyword 'null'                         |
| `keyword_CONTINUE`                 | `continue`                 | Keyword 'continue'                     |
| `keyword_BREAK`                    | `break`                    | Keyword 'break'                        |
| `keyword_PRINT`                    | `print`                    | Keyword 'print'                        |
| `type_BYTE`                        | `byte`                     | Type keyword 'byte'                    |
| `type_INT`                         | `int`                      | Type keyword 'int'                     |
| `type_FLOAT`                       | `float`                    | Type keyword 'float'                   |
| `type_DOUBLE`                      | `double`                   | Type keyword 'double'                  |
| `type_CHAR`                        | `char`                     | Type keyword 'char'                    |
| `type_STRING`                      | `string`                   | Type keyword 'string'                  |
| `type_BOOL`                        | `bool`                     | Type keyword 'bool'                    |
| `type_LONG`                        | `long`                     | Type keyword 'long'                    |
| `literal_BOOLEAN_TRUE`             | `true`                     | Boolean literal 'true'                 |
| `literal_BOOLEAN_FALSE`            | `false`                    | Boolean literal 'false'                |
| `IDENTIFIER`                       | `[a-zA-Z_][a-zA-Z0-9_]*`   | Identifiers (variables, classes, etc.) |
| `literal_INTEGER`                  | `[0-9]+`                   | Integer numerical literal              |
| `literal_FLOAT`                    | `[0-9]+\.[0-9]+`           | Floating-point numerical literal       |
| `literal_CHAR`                     | `'[^']'`                   | Character literal                      |
| `literal_STRING`                   | `"[^"]*"`                  | String literal                         |
| `operator_ARITHMETIC_PLUS`         | `\+`                       | Arithmetic operator '+'                |
| `operator_ARITHMETIC_MINUS`        | `-`                        | Arithmetic operator '-'                |
| `operator_ARITHMETIC_MULTIPLY`     | `\*`                       | Arithmetic operator '*'                |
| `operator_ARITHMETIC_DIVIDE`       | `/`                        | Arithmetic operator '/'                |
| `operator_ARITHMETIC_MODULO`       | `%`                        | Arithmetic operator '%'                |
| `operator_ASSIGNMENT_ASSIGN`       | `=`                        | Assignment operator '='                |
| `operator_ASSIGNMENT_PLUS_ASSIGN`  | `\+=`                      | Assignment operator '+='               |
| `operator_ASSIGNMENT_MINUS_ASSIGN` | `-=`                       | Assignment operator '-='               |
| `operator_ASSIGNMENT_MULT_ASSIGN`  | `\*=`                      | Assignment operator '*='               |
| `operator_ASSIGNMENT_DIV_ASSIGN`   | `/=`                       | Assignment operator '/='               |
| `operator_ASSIGNMENT_MOD_ASSIGN`   | `%=`                       | Assignment operator '%='               |
| `operator_COMPARISON_EQUAL`        | `==`                       | Comparison operator '=='               |
| `operator_COMPARISON_NOT_EQUAL`    | `!=`                       | Comparison operator '!='               |
| `operator_COMPARISON_GREATER`      | `>`                        | Comparison operator '>'                |
| `operator_COMPARISON_LESS`         | `<`                        | Comparison operator '<'                |
| `operator_COMPARISON_GREATER_EQ`   | `>=`                       | Comparison operator '>='               |
| `operator_COMPARISON_LESS_EQ`      | `<=`                       | Comparison operator '<='               |
| `operator_LOGICAL_AND`             | `and`                      | Logical operator 'and'                 |
| `operator_LOGICAL_OR`              | `or`                       | Logical operator 'or'                  |
| `operator_LOGICAL_NOT`             | `not`                      | Logical operator 'not'                 |
| `operator_BITWISE_AND`             | `&`                        | Bitwise operator '&'                   |
| `bracket_LEFT_PAREN`               | `\(`                       | Bracket '('                            |
| `bracket_RIGHT_PAREN`              | `\)`                       | Bracket ')'                            |
| `bracket_LEFT_SQUARE`              | `\[`                       | Bracket '['                            |
| `bracket_RIGHT_SQUARE`             | `\]`                       | Bracket ']'                            |
| `separator_COMMA`                  | `,`                        | Separator ','                          |
| `separator_SEMICOLON`              | `;`                        | Separator ';'                          |
| `separator_DOT`                    | `\.`                       | Separator '.' (member access)          |
| `separator_COLON`                  | `:`                        | Separator ':'                          |
| `COMMENT`                          | `#.*`                      | Single-line comment                    |
| `WHITESPACE`                       | `[ \t\r\n]+`               | Whitespace characters (ignored)        |

---

## Grammar

### 1. Notation

### 2. Grammar Definition

---

## Tools and Dependencies

---

## Usage Instructions

---

## Usage Example

Below is a simple example demonstrating some features of the Chubby language.

**Sample Input (`example.cbb`):**

```python
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
        if a > b and b < 30 then
            # Assuming 'print' is a built-in or globally accessible function
            print("Complex condition is true");
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
                print("Nested loop: " + i); # Assuming string concatenation with int works
                break;
            endwhile
        endfor

        # Test function calls and return values (already tested above)
        int result1 = add(a, b);
        int result2 = multiply(a, b);
        print("Addition result: " + result1);
        print("Multiplication result: " + result2);

        # Test array operations
        int[] singleArray = new int[5];
        singleArray[0] = 10;
        singleArray[1] = 20;
        print("Single array element 0: " + singleArray[0]);

        int[][] multiArray = new int[3][3];
        multiArray[0][0] = 1;
        multiArray[1][1] = 2;
        print("Multi array element [1][1]: " + multiArray[1][1]);

        # Test error handling
        try
            print("Attempting division by zero...");
            int error = divide(10, 0); # Calling the static method
            print("This should not be printed.");
        catch (Exception e)
            print("Division by zero caught!");
        endtry

        # Test object-oriented features - Instantiating the separate Person class
        Person person = new Person("John", 30);
        person.get(); # Calling an instance method on the Person object

        print("Execution finished.");
    endfunction # End of main method

    # Utility function for addition (now a static method of ExampleApp)
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
```
