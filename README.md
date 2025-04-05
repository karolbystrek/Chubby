# Chubby

Our own programming language made for TKiK university course.

---

## Authors

* **Karol Bystrek** - [karbystrek@student.agh.edu.pl](mailto:karbystrek@student.agh.edu.pl)
* **Patryk Chamera** - [pchamera@student.agh.edu.pl](mailto:pchamera@student.agh.edu.pl)

---

## Project Specification

### 1. General Goals

The main goal of the Chubby project is to design and implement a custom, object-oriented programming language as part of the TKiK university course. This involves defining the language's syntax and semantics, developing a compiler, and exploring concepts like lexical analysis, parsing, memory management, and error handling.

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
| `keyword_ENUM`                     | `enum`                     | Keyword 'enum'                         |
| `keyword_ENDENUM`                  | `endenum`                  | Keyword 'endenum'                      |
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
| `operator_ARITHMETIC_INCREMENT`    | `\+\+`                     | Arithmetic operator '++'               |
| `operator_ARITHMETIC_DECREMENT`    | `--`                       | Arithmetic operator '--'               |
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
| `operator_BITWISE_AND`             | `&`                        | Bitwise operator '&'                   |
| `operator_BITWISE_OR`              | `\|`                       | Bitwise operator '\|'                  |
| `operator_BITWISE_XOR`             | `\^`                       | Bitwise operator '^'                   |
| `operator_BITWISE_LEFT_SHIFT`      | `<<`                       | Bitwise operator '<<'                  |
| `operator_BITWISE_RIGHT_SHIFT`     | `>>`                       | Bitwise operator '>>'                  |
| `bracket_LEFT_PAREN`               | `\(`                       | Bracket '('                            |
| `bracket_RIGHT_PAREN`              | `\)`                       | Bracket ')'                            |
| `bracket_LEFT_SQUARE`              | `\[`                       | Bracket '['                            |
| `bracket_RIGHT_SQUARE`             | `\]`                       | Bracket ']'                            |
| `bracket_LEFT_BRACE`               | `\{`                       | Bracket '{'                            |
| `bracket_RIGHT_BRACE`              | `\}`                       | Bracket '}'                            |
| `separator_COMMA`                  | `,`                        | Separator ','                          |
| `separator_SEMICOLON`              | `;`                        | Separator ';'                          |
| `separator_DOT`                    | `\.`                       | Separator '.' (member access)          |
| `separator_COLON`                  | `:`                        | Separator ':'                          |
| `COMMENT`                          | `#.*`                      | Single-line comment                    |
| `WHITESPACE`                       | `[ \t\r\n]+`               | Whitespace characters (ignored)        |

---

## Grammar

### 1. Notation

<!-- * **BNF (Backus-Naur Form)** - Extended syntax used below for clarity (e.g., `*` for zero or more, `?` for optional).
    **[Placeholder: Confirm or change if using a different notation like ANTLR's]** -->

### 2. Grammar Definition

---

## Tools and Dependencies

<!-- * **Scanner/Parser Generators:** **[Placeholder: e.g., ANTLR 4, Flex + Bison, None (Manual Implementation)]**
* **External Packages/Libraries:** **[Placeholder: e.g., LLVM C++ API 15.x, None]**
* **Build System:** **[Placeholder: e.g., Make, CMake, Gradle, Python setuptools]**
* **Compiler/Interpreter for Build:** **[Placeholder: Specify required language/compiler version used to build Chubby, e.g., GCC 11+, Python 3.9+, Java 17+]** -->

---

## Usage Instructions

<!-- 1. **Prerequisites:** Ensure you have installed the necessary tools and dependencies listed above. **[Placeholder: Add specific prerequisites, e.g., `make`, `g++`, `python3`, `llvm-dev`]**.
2. **Clone the repository:**

    ```bash
    git clone [Placeholder: repository-url]
    cd [Placeholder: repository-directory]
    ```

3. **Build the project:** Execute the appropriate build command for your setup.

    ```bash
    # [Placeholder: Provide the actual build command]
    # Example: make
    # Example: cmake .. && make
    # Example: python setup.py build
    ```

4. **Run the program:** Use the compiled executable or interpreter script to process a Chubby source file.

    ```bash
    # [Placeholder: Provide the command to run your compiler/interpreter]
    # Example Compiler: ./chubbyc source_file.chubby -o output_target
    # Example Interpreter: python chubby_interpreter.py script_file.chubby
    ./[executable_name] [input_file.chubby] [optional_arguments]
    ``` -->

---

## Usage Example

Below is a simple example demonstrating some features of the Chubby language.

**Sample Input (`example.cbb`):**

```ada
function public main() : void
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
    int sum = a + b;
    int difference = b - a;
    int product = a * b;
    int quotient = b / a;
    int modulo = b % a;

    # Test increment and decrement
    a++;
    b--;

    # Test compound assignment operators
    int x = 5;
    x += 3;   # x is now 8
    x -= 2;   # x is now 6
    x *= 2;   # x is now 12
    x /= 3;   # x is now 4
    x %= 2;   # x is now 0

    # Test comparison and logical operators
    if (a > b and b < 30) then
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
    for (int i = 0; i < 3; i++) then
        while (i < 2) then
            print("Nested loop: " + i);
            break;
        endwhile
    endfor

    # Test function calls and return values
    int result1 = add(a, b);
    int result2 = multiply(a, b);
    print("Addition result: " + result1);
    print("Multiplication result: " + result2);

    # Test array operations
    int[] singleArray = new int[5];
    singleArray[0] = 10;
    singleArray[1] = 20;

    int[][] multiArray = new int[3][3];
    multiArray[0][0] = 1;
    multiArray[1][1] = 2;

    # Test error handling
    try
        int error = divide(10, 0);
    catch
        print("Division by zero caught!");
    endtry

    # Test object-oriented features
    Person person = new Person("John", 30);
    person.get();
endfunction

function public add(int x, int y) : int
    return x + y;
endfunction

function public multiply(int x, int y) : int
    return x * y;
endfunction

function public divide(int x, int y) : int
    return x / y;
endfunction

public class Person implements Entity
    string name;
    int age;

    constructor Person(string name, int age)
        this.name = name;
        this.age = age;
    endconstructor

    function public get() : void
        print("Hello, my name is " + name + " and I am " + age + " years old");
    endfunction
endclass
```
