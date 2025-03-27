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

| Token Type         | Lexeme/Pattern                               | Description                                         |
| :----------------- | :------------------------------------------- | :-------------------------------------------------- |
| `KEYWORD`          | `class`, `endclass`, `function`, `endfunction`, `constructor`, `endconstructor`, `enum`, `endenum`, `if`, `then`, `elsif`, `else`, `endif`, `for`, `endfor`, `while`, `endwhile`, `try`, `catch`, `endtry`, `throw`, `return`, `extends`, `implements`, `public`, `protected`, `private`, `const`, `override`, `static`, `import`, `new`, `this`, `void`, `null`, `continue`, `break` | Reserved language keywords                          |
| `TYPE_KEYWORD`     | `byte`, `int`, `float`, `double`, `char`, `string`, `bool`, `long` | Built-in primitive type keywords                  |
| `BOOLEAN_LITERAL`  | `true`, `false`                              | Boolean literal values                              |
| `IDENTIFIER`               | `[a-zA-Z_][a-zA-Z0-9_]*`                     | Identifiers (variables, classes, methods, etc.)    |
| `INTEGER_LITERAL`  | `[0-9]+`                                     | Integer numerical literals                          |
| `FLOAT_LITERAL`    | `[0-9]+.[0-9]+`                              | Floating-point numerical literals                   |
| `CHAR_LITERAL`     | `'[^']'`                                     | Character literals (e.g., 'a')                      |
| `STRING_LITERAL`   | `"[^"]*"`                                    | String literals (e.g., "Hello")                     |
| `ARITHMETIC_OP`    | `+`, `-`, `*`, `/`, `%`, `++`, `--`          | Arithmetic operators                                |
| `ASSIGNMENT_OP`    | `=`, `+=`, `-=`, `*=`, `/=`, `%=`            | Assignment operators                                |
| `COMPARISON_OP`    | `==`, `!=`, `>`, `<`, `>=`, `<=`             | Comparison operators                                |
| `LOGICAL_OP`       | `and`, `or`                                  | Logical operators                                   |
| `BITWISE_OP`       | `&`, `\|`,`^`,`<<`,`>>`                   | Bitwise operators                                   |
| `BRACKETS`        | `(`, `)`, `[`, `]`, `{`, `}`                 | Parentheses, brackets, braces                       |
| `SEPARATOR`        | `,`, `;`, `.`, `:`                                | Comma, semicolon, dot (member access), colon               |
| `COMMENT`   | `#.*`                                          | Single-line comment                     |
| `WHITESPACE`       | `[ \t\r\n]+`                                 | Whitespace characters (ignored, separates tokens)   |
<!-- | `COMMENT_CONTENT`  | `.*` (following `#` until newline)           | Text content of a comment (ignored)                 | -->
<!-- | `ACCESS_MODIFIER`  | `public`, `protected`, `private`             | Access control modifiers                            | -->
<!-- | `TERNARY_OP`       | `?`, `:`                                     | Parts of the ternary conditional operator           | -->

---

## Grammar

### 1. Notation

<!-- * **BNF (Backus-Naur Form)** - Extended syntax used below for clarity (e.g., `*` for zero or more, `?` for optional).
    **[Placeholder: Confirm or change if using a different notation like ANTLR's]** -->

### 2. Grammar Definition

<!-- ```bnf
<program> ::= <import_statement>* <declaration>*

<import_statement> ::= "import" <qualified_identifier> ";"

<declaration> ::= <class_declaration> | <enum_declaration> | <function_declaration> // Assuming top-level functions are allowed, else remove

<access_modifier> ::= "public" | "protected" | "private"

<class_declaration> ::= "class" <access_modifier>? <identifier> <extends_clause>? <implements_clause>?
                          <class_member>*
                        "endclass"

<extends_clause> ::= "extends" <identifier>

<implements_clause> ::= "implements" <identifier> ("," <identifier>)*

<class_member> ::= <field_declaration> | <constructor_declaration> | <method_declaration>

<field_declaration> ::= <access_modifier>? "static"? "const"? <type> <identifier> ("=" <expression>)? ";"

<constructor_declaration> ::= <access_modifier>? "constructor" <identifier> "(" <parameter_list>? ")"
                                <statement>*
                              "endconstructor"

<method_declaration> ::= "function" <access_modifier>? "static"? "override"? <identifier> "(" <parameter_list>? ")" ":" <return_type>
                           <statement>*
                         "endfunction"

<parameter_list> ::= <parameter> ("," <parameter>)*
<parameter> ::= <type> <identifier>

<return_type> ::= <type> | "void"

<type> ::= <primitive_type> | <identifier> (<array_specifier>)?
<primitive_type> ::= "byte" | "int" | "float" | "double" | "char" | "string" | "bool" | "long"
<array_specifier> ::= "[" "]" ("[" "]")* // For multi-dimensional arrays

<enum_declaration> ::= "enum" <access_modifier>? <identifier>
                         <enum_constant> ("," <enum_constant>)*
                         <enum_member>* // Optionally allow methods/fields inside enums
                       "endenum"
<enum_constant> ::= <identifier>
<enum_member> ::= <method_declaration> // Simplified, could include fields

<statement> ::= <variable_declaration_statement>
              | <assignment_statement>
              | <if_statement>
              | <for_loop>
              | <while_loop>
              | <return_statement>
              | <expression_statement> // e.g., function calls like print(...)
              | <try_catch_statement>
              | <throw_statement>
              | <block_statement> // Potentially needed if explicit blocks { } are desired beyond control structures

<variable_declaration_statement> ::= <type> <identifier> ("=" <expression>)? ";"

<assignment_statement> ::= <lvalue> <assignment_operator> <expression> ";"
<lvalue> ::= <identifier> | <array_access> | <member_access> // What can be assigned to
<assignment_operator> ::= "=" | "+=" | "-=" | "*=" | "/=" | "%="

<if_statement> ::= "if" "(" <expression> ")" "then"
                      <statement>*
                   ("elsif" "(" <expression> ")" "then" <statement>* )*
                   ("else" <statement>* )?
                   "endif"

<for_loop> ::= "for" "(" <for_init>? ";" <expression>? ";" <for_update>? ")" "then"
                  <statement>*
               "endfor"
<for_init> ::= <variable_declaration_statement> | <expression_list>
<for_update> ::= <expression_list>

<while_loop> ::= "while" "(" <expression> ")" "then"
                    <statement>*
                 "endwhile"

<return_statement> ::= "return" <expression>? ";"

<try_catch_statement> ::= "try"
                             <statement>*
                          "catch" "(" <identifier> <identifier> ")" // e.g. (ExceptionType e)
                             <statement>*
                          "endtry"

<throw_statement> ::= "throw" <expression> ";"

<expression_statement> ::= <expression> ";"

<expression> ::= <assignment_expression> // Lowest precedence

<assignment_expression> ::= <conditional_expression> | <lvalue> <assignment_operator> <expression>

<conditional_expression> ::= <logical_or_expression> ( "?" <expression> ":" <conditional_expression> )?

<logical_or_expression> ::= <logical_and_expression> ( "or" <logical_and_expression> )*

<logical_and_expression> ::= <bitwise_or_expression> ( "and" <bitwise_or_expression> )*

<bitwise_or_expression> ::= <bitwise_xor_expression> ( "|" <bitwise_xor_expression> )*

<bitwise_xor_expression> ::= <bitwise_and_expression> ( "^" <bitwise_and_expression> )*

<bitwise_and_expression> ::= <equality_expression> ( "&" <equality_expression> )*

<equality_expression> ::= <relational_expression> ( ("==" | "!=") <relational_expression> )*

<relational_expression> ::= <shift_expression> ( (">" | "<" | ">=" | "<=") <shift_expression> )*

<shift_expression> ::= <additive_expression> ( ("<<" | ">>") <additive_expression> )*

<additive_expression> ::= <multiplicative_expression> ( ("+" | "-") <multiplicative_expression> )*

<multiplicative_expression> ::= <unary_expression> ( ("*" | "/" | "%") <unary_expression> )*

<unary_expression> ::= <postfix_expression>
                     | ("+" | "-" | "~") <unary_expression>
                     | ("++" | "--") <lvalue> // Pre-increment/decrement

<postfix_expression> ::= <primary_expression>
                       | <postfix_expression> "++" // Post-increment
                       | <postfix_expression> "--" // Post-decrement
                       | <postfix_expression> "[" <expression> "]" // Array access
                       | <postfix_expression> "." <identifier> // Member access
                       | <postfix_expression> "(" <argument_list>? ")" // Function call

<primary_expression> ::= <literal>
                       | <identifier>
                       | "this"
                       | "(" <expression> ")"
                       | "new" <type> ( "(" <argument_list>? ")" | "[" <expression> "]" ("[" <expression> "]")* ) // Object or Array creation

<literal> ::= <INTEGER_LITERAL> | <FLOAT_LITERAL> | <CHAR_LITERAL> | <STRING_LITERAL> | <BOOLEAN_LITERAL> | "null" // Assuming null exists

<argument_list> ::= <expression> ("," <expression>)*
<expression_list> ::= <expression> ("," <expression>)*

<qualified_identifier> ::= <identifier> ("." <identifier>)* // For imports like com.example.MyClass

// Note: This grammar is illustrative and might need refinement based on specific language ambiguities or detailed features.
// Error handling details (e.g., specific exception types) are simplified.
// Explicit block statements might be needed depending on scope rules. -->

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
