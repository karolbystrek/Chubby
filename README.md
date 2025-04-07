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

The planned output is a compiler for the Chubby language that transforms `.cbb` source code into **JVM bytecode** .

### 4. Implementation Language

* The Chubby translator is implemented in the **Java** programming language.

### 5. Scanner/Parser Implementation Method

The scanner and parser are generated using the **ANTLR4** generator, integrated with the Java
implementation.

---

## Token Description

The tokens of the Chubby language are defined by the following ANTLR4 lexer rules. The order of definition (especially
keywords before `IDENTIFIER`) is important for correct recognition.

| Token Name (ANTLR) | Literal/Pattern (ANTLR)                                                                                                  | Description                             |
|:-------------------|:-------------------------------------------------------------------------------------------------------------------------|:----------------------------------------|
| `CLASS`            | `'class'`                                                                                                                | Keyword 'class'                         |
| `ENDCLASS`         | `'endclass'`                                                                                                             | Keyword 'endclass'                      |
| `FUNCTION`         | `'function'`                                                                                                             | Keyword 'function'                      |
| `ENDFUNCTION`      | `'endfunction'`                                                                                                          | Keyword 'endfunction'                   |
| `CONSTRUCTOR`      | `'constructor'`                                                                                                          | Keyword 'constructor'                   |
| `ENDCONSTRUCTOR`   | `'endconstructor'`                                                                                                       | Keyword 'endconstructor'                |
| `IF`               | `'if'`                                                                                                                   | Keyword 'if'                            |
| `THEN`             | `'then'`                                                                                                                 | Keyword 'then'                          |
| `ELSIF`            | `'elsif'`                                                                                                                | Keyword 'elsif'                         |
| `ELSE`             | `'else'`                                                                                                                 | Keyword 'else'                          |
| `ENDIF`            | `'endif'`                                                                                                                | Keyword 'endif'                         |
| `FOR`              | `'for'`                                                                                                                  | Keyword 'for'                           |
| `ENDFOR`           | `'endfor'`                                                                                                               | Keyword 'endfor'                        |
| `WHILE`            | `'while'`                                                                                                                | Keyword 'while'                         |
| `ENDWHILE`         | `'endwhile'`                                                                                                             | Keyword 'endwhile'                      |
| `CONTINUE`         | `'continue'`                                                                                                             | Keyword 'continue'                      |
| `BREAK`            | `'break'`                                                                                                                | Keyword 'break'                         |
| `TRY`              | `'try'`                                                                                                                  | Keyword 'try'                           |
| `CATCH`            | `'catch'`                                                                                                                | Keyword 'catch'                         |
| `FINALLY`          | `'finally'`                                                                                                              | Keyword 'finally'                       |
| `ENDTRY`           | `'endtry'`                                                                                                               | Keyword 'endtry'                        |
| `THROW`            | `'throw'`                                                                                                                | Keyword 'throw'                         |
| `RETURN`           | `'return'`                                                                                                               | Keyword 'return'                        |
| `EXTENDS`          | `'extends'`                                                                                                              | Keyword 'extends'                       |
| `IMPLEMENTS`       | `'implements'`                                                                                                           | Keyword 'implements'                    |
| `PUBLIC`           | `'public'`                                                                                                               | Keyword 'public'                        |
| `PRIVATE`          | `'private'`                                                                                                              | Keyword 'private'                       |
| `PROTECTED`        | `'protected'`                                                                                                            | Keyword 'protected'                     |
| `CONST`            | `'const'`                                                                                                                | Keyword 'const'                         |
| `STATIC`           | `'static'`                                                                                                               | Keyword 'static'                        |
| `OVERRIDE`         | `'override'`                                                                                                             | Keyword 'override'                      |
| `IMPORT`           | `'import'`                                                                                                               | Keyword 'import'                        |
| `NEW`              | `'new'`                                                                                                                  | Keyword 'new'                           |
| `THIS`             | `'this'`                                                                                                                 | Keyword 'this'                          |
| `VOID`             | `'void'`                                                                                                                 | Keyword 'void'                          |
| `NULL`             | `'null'`                                                                                                                 | Keyword 'null'                          |
| `BYTE`             | `'byte'`                                                                                                                 | Type keyword 'byte'                     |
| `BOOL`             | `'bool'`                                                                                                                 | Type keyword 'bool'                     |
| `INT`              | `'int'`                                                                                                                  | Type keyword 'int'                      |
| `FLOAT`            | `'float'`                                                                                                                | Type keyword 'float'                    |
| `DOUBLE`           | `'double'`                                                                                                               | Type keyword 'double'                   |
| `CHAR`             | `'char'`                                                                                                                 | Type keyword 'char'                     |
| `STRING`           | `'string'`                                                                                                               | Type keyword 'string'                   |
| `LONG`             | `'long'`                                                                                                                 | Type keyword 'long'                     |
| `AND`              | `'and'`                                                                                                                  | Logical operator 'and'                  |
| `OR`               | `'or'`                                                                                                                   | Logical operator 'or'                   |
| `NOT`              | `'not'`                                                                                                                  | Logical operator 'not'                  |
| `BOOL_LITERAL`     | `'true' \| 'false'`                                                                                                      | Boolean literal ('true' or 'false')     |
| `PLUS_ASSIGN`      | `'+='`                                                                                                                   | Assignment operator '+='                |
| `MINUS_ASSIGN`     | `'-='`                                                                                                                   | Assignment operator '-='                |
| `MULTIPLY_ASSIGN`  | `'*='`                                                                                                                   | Assignment operator '*='                |
| `DIVIDE_ASSIGN`    | `'/='`                                                                                                                   | Assignment operator '/='                |
| `MODULO_ASSIGN`    | `'%='`                                                                                                                   | Assignment operator '%='                |
| `EQUAL`            | `'=='`                                                                                                                   | Comparison operator '=='                |
| `NOT_EQUAL`        | `'!='`                                                                                                                   | Comparison operator '!='                |
| `LESS_EQUAL`       | `'<='`                                                                                                                   | Comparison operator '<='                |
| `GREATER_EQUAL`    | `'>='`                                                                                                                   | Comparison operator '>='                |
| `PLUS`             | `'+'`                                                                                                                    | Arithmetic operator '+'                 |
| `MINUS`            | `'-'`                                                                                                                    | Arithmetic operator '-'                 |
| `MULTIPLY`         | `'*'`                                                                                                                    | Arithmetic operator '*'                 |
| `DIVIDE`           | `'/'`                                                                                                                    | Arithmetic operator '/'                 |
| `MODULO`           | `'%'`                                                                                                                    | Arithmetic operator '%'                 |
| `ASSIGN`           | `'='`                                                                                                                    | Assignment operator '='                 |
| `LESS`             | `'<'`                                                                                                                    | Comparison operator '<'                 |
| `GREATER`          | `'>'`                                                                                                                    | Comparison operator '>'                 |
| `LEFT_PAREN`       | `'('`                                                                                                                    | Left parenthesis '('                    |
| `RIGHT_PAREN`      | `')'`                                                                                                                    | Right parenthesis ')'                   |
| `LEFT_SQUARE`      | `'['`                                                                                                                    | Left square bracket '['                 |
| `RIGHT_SQUARE`     | `']'`                                                                                                                    | Right square bracket ']'                |
| `COMMA`            | `','`                                                                                                                    | Comma separator ','                     |
| `SEMICOLON`        | `';'`                                                                                                                    | Semicolon separator ';'                 |
| `DOT`              | `'.'`                                                                                                                    | Dot separator '.' (member access)       |
| `COLON`            | `':'`                                                                                                                    | Colon separator ':'                     |
| `FLOAT_LITERAL`    | `([0-9]+ '.' [0-9]* \| '.' [0-9]+) ([eE] [+\-]? [0-9]+)? [fF]` <br> `\| [0-9]+ ([eE] [+\-]? [0-9]+)? [fF]`               | Float literal                           |
| `DOUBLE_LITERAL`   | `([0-9]+ '.' [0-9]* \| '.' [0-9]+) ([eE] [+\-]? [0-9]+)? [dD]?` <br> `\| [0-9]+ [eE] [+\-]? [0-9]+ [dD]? \| [0-9]+ [dD]` | Double literal                          |
| `INTEGER_LITERAL`  | `[0-9]+`                                                                                                                 | Integer literal                         |
| `CHAR_LITERAL`     | `'\'' ( '\\' [nt\\'"] \| ~['\\] ) '\''`                                                                                  | Character literal                       |
| `STRING_LITERAL`   | `'"' ( ~["\\] \| '\\' . )* '"'`                                                                                          | String literal                          |
| `IDENTIFIER`       | `[a-zA-Z_][a-zA-Z0-9_]*`                                                                                                 | Identifier (variable, class name, etc.) |
| `LINE_COMMENT`     | `'#' ~[\r\n]* -> skip`                                                                                                   | Single-line comment (ignored)           |
| `WS`               | `[ \t\r\n]+ -> skip`                                                                                                     | Whitespace (ignored)                    |

---

## Grammar

### 1. Notation

The Chubby language grammar is defined using the **ANTLR4** parser generator notation.

### 2. Grammar Definition

The full ANTLR4 grammar definition for Chubby can be found in the following file:
**[Chubby.g4](src/main/antlr4/com/karolbystrek/chubbycompiler/Chubby.g4)**

---

## Tools and Dependencies

* **Implementation Language:** Java (JDK 21)
* **Scanner/Parser Generator:** ANTLR4 (4.13.1)
* **Build System:** Maven
* **Other Packages:** JUnit (5.10.2)

---

## Usage Instructions

1. **Prerequisites:**
    * Java Development Kit (JDK) version 21 installed.
    * Maven installed.
    * ANTLR4 Java Runtime library (if not managed by the build system).

2. **Compiling the Translator:**
   ```bash
   mvn clean package
   ```

3. **Running the Compiler:**
   ```bash
   java -jar target/ChubbyCompiler.jar input_file.cbb
   ```
   Where `input_file.cbb` is your Chubby source code file.

---

## Usage Example

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
        if (a > b and a < b) then # Requires parentheses
            print("Complex condition is true"); // Assuming print exists
        elsif (a > b) then # Requires parentheses
            print("a>b");
        endif

        if (a == b or a != b) then # Requires parentheses
            print("Equality and inequality test");
        endif

        # Test nested conditionals
        if (integerVar > 40) then # Requires parentheses
            if (floatVar < 4.0) then # Requires parentheses
                print("Nested condition met");
            else
                print("Outer condition true, inner false");
            endif
        endif

        # Test different types of loops
        for (int i = 0; i < 3; i += 1) then
            while (i < 2) then # Requires parentheses
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
        catch (Exception e) # Assuming a base Exception type exists
            print("Division by zero caught!");
        endtry

        Person person = new Person("John", 30);
        person.get(); # Calling an instance method

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
class public Person implements Entity # Assuming Entity interface/class exists
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
