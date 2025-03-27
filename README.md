# Chubby

Our own programming language made for TKiK university course.

---

## Authors

* **Karol Bystrek** - [karbystrek@student.agh.edu.pl](mailto:karbystrek@student.agh.edu.pl)
* **Patryk Chamera** - [pchamera@student.agh.edu.pl](mailto:pchamera@student.agh.edu.pl)

---

## Project Specification

### 1. General Goals

[Describe the main objectives and aims of the program.]

### 2. Translator Type

* [Specify if it's an **Interpreter** or a **Compiler**.]

### 3. Planned Output

[Describe the expected result or output of the program. Examples:]

* *e.g., A compiler for language X generating LLVM code.*
* *e.g., An interpreter for language Y handling matrix operations and displaying results.*
* *e.g., A converter (compiler) from Pascal to C.*
* *e.g., A simulator (interpreter) for AVR assembler showing register/memory state during step-by-step execution.*

### 4. Implementation Language

* [Specify the programming language used to build this project, e.g., Python, Java, C++, etc.]

### 5. Scanner/Parser Implementation Method

* [Describe how the lexical analysis (scanning) and syntax analysis (parsing) are implemented.]
* *e.g., Manually implemented scanner and recursive descent parser.*
* *e.g., Using scanner/parser generators:*
  * **Scanner Generator:** [Specify tool, e.g., Flex, JFlex]
  * **Parser Generator:** [Specify tool, e.g., Bison, ANTLR, Yacc]

---

## Token Description

[Provide a description of the language's tokens. This can be done using:]

* **A Table:**

    | Token Type | Lexeme/Pattern        | Description                     |
    | :--------- | :-------------------- | :------------------------------ |
    | `KEYWORD`  | `if`, `else`, `while` | Reserved language keywords      |
    | `ID`       | `[a-zA-Z_][a-zA-Z0-9_]*`| Identifiers (variables, etc.) |
    | `NUMBER`   | `[0-9]+`              | Integer literals                |
    | `OPERATOR` | `+`, `-`, `*`, `/`, `=` | Arithmetic/Assignment operators |
    | ...        | ...                   | ...                             |

* **A List:**
  * `KEYWORD`: `if`, `else`, `while`, ...
  * `IDENTIFIER`: Matches `[a-zA-Z_][a-zA-Z0-9_]*`
  * `INTEGER_LITERAL`: Matches `[0-9]+`
  * ...

* **Generator Notation:**

    ```[generator-syntax]
    // Example using Flex-like syntax
    %%
    if | else | while   { return KEYWORD; }
    [a-zA-Z_][a-zA-Z0-9_]* { return ID; }
    [0-9]+              { return NUMBER; }
    "+" | "-" | "*" | "/" | "=" { return OPERATOR; }
    // ... other token rules
    %%
    ```

---

## Grammar

### 1. Notation

* [Specify the notation used, e.g., **BNF (Backus-Naur Form)** or **Parser Generator Notation (Yacc, Bison, ANTLR, etc.)**]

### 2. Grammar Definition

[Provide the context-free grammar for the language, without semantic actions. Use a code block for clarity.]

* **Using Standard Notation (e.g., BNF):**

    ```bnf
    <program> ::= <statement_list>
    <statement_list> ::= <statement> | <statement> <statement_list>
    <statement> ::= <assignment_statement> | <if_statement> | ...
    <assignment_statement> ::= <identifier> '=' <expression> ';'
    <expression> ::= <term> | <expression> '+' <term> | <expression> '-' <term>
    <term> ::= <factor> | <term> '*' <factor> | <term> '/' <factor>
    <factor> ::= <identifier> | <number> | '(' <expression> ')'
    // ... rest of the grammar rules
    ```

* **Using Generator Notation (e.g., Yacc/Bison):**

    ```yacc
    %token ID NUMBER IF ELSE WHILE PLUS MINUS TIMES DIVIDE ASSIGN LPAREN RPAREN SEMI
    // ... other token declarations

    %%
    program: statement_list;

    statement_list: statement
                  | statement statement_list
                  ;

    statement: assignment_statement
             | if_statement
             // | other_statements ...
             ;

    assignment_statement: ID ASSIGN expression SEMI;

    expression: term
              | expression PLUS term
              | expression MINUS term
              ;

    term: factor
        | term TIMES factor
        | term DIVIDE factor
        ;

    factor: ID
          | NUMBER
          | LPAREN expression RPAREN
          ;

    // ... rest of the grammar rules
    %%
    ```

---

## Tools and Dependencies

* **Scanner/Parser Generators:** [List any generators used, e.g., Flex, Bison, ANTLR]
* **External Packages/Libraries:** [List any third-party libraries required, e.g., LLVM library, specific data structure libraries]
* **Build System:** [e.g., Make, CMake, Maven, Gradle]
* **Compiler/Interpreter:** [Specify the required version, e.g., GCC 9+, Python 3.8+, Java 11+]

---

## Usage Instructions

[Provide clear, step-by-step instructions on how to build/compile and run the program.]

1. **Prerequisites:** Ensure you have installed [List prerequisites, e.g., `make`, `gcc`, `flex`, `bison`, `python3`].
2. **Clone the repository:**

    ```bash
    git clone [repository-url]
    cd [repository-directory]
    ```

3. **Build the project:**

    ```bash
    make # Or relevant build command
    ```

4. **Run the program:**

    ```bash
    ./[executable_name] [input_file] [optional_arguments]
    # e.g., ./mycompiler source.lang -o output.llvm
    # e.g., python myinterpreter.py script.myilang
    ```

---

## Usage Example

[Provide a simple but illustrative example of how to use the program with sample input and expected output.]

**Sample Input (`example.src`):**

```[language-syntax]
// Example code in your defined language
variable = 10 + 5 * 2;
print variable;
