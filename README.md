# Chubby

Our own programming language made for *Teoria Kompilacji i Kompilatory* university course.

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
| `TRUE`             | `'true'`                                                                                                                 | Keyword 'true'                          |
| `FALSE`            | `'false'`                                                                                                                | Keyword 'false'                         |
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

[//]: # (Sample Input &#40;`example.cbb`&#41;:**)
Sample Input: **[example.cbb](example.cbb)**
