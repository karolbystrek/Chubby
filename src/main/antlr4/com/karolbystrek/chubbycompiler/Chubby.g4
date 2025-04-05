grammar Chubby;

program
    : import_statement* class_definition+ EOF
    ;

import_statement
    : IMPORT qualified_identifier SEMICOLON
    ;

qualified_identifier
    : IDENTIFIER ( DOT IDENTIFIER )*
    ;

class_definition
    : CLASS IDENTIFIER
    ( EXTENDS qualified_identifier )?
    ( IMPLEMENTS qualified_identifier ( COMMA qualified_identifier )* )?
    class_body
    ENDCLASS
    ;

class_body
    : class_member*
    ;

class_member
    : function_definition
    | constructor_definition
    | variable_definition
    ;

constructor_definition
    : CONSTRUCTOR /* Placeholder */
    ENDCONSTRUCTOR
    ;

variable_definition
    : visibility_modifier? type_specifier IDENTIFIER ( ASSIGN expression )? SEMICOLON
    ;

function_definition
    : FUNCTION
    visibility_modifier?
    IDENTIFIER
    LEFT_PAREN parameter_list? RIGHT_PAREN
    COLON return_type
    function_body
    ENDFUNCTION
    ;

visibility_modifier
    : PUBLIC
    | PRIVATE
    | PROTECTED
    ;

parameter_list
    : parameter ( COMMA parameter )*
    ;

parameter
    : type_specifier IDENTIFIER
    ;

type_specifier
    : BYTE | BOOL | INT | FLOAT | DOUBLE | CHAR | STRING | LONG
    | qualified_identifier
    ;

return_type
    : type_specifier
    | VOID
    ;

function_body
    : statement*
    ;

statement
    : block_statement
    | simple_statement SEMICOLON
    ;

block_statement
    : if_statement
    | for_statement
    | while_statement
    | try_catch_statement
    ;

simple_statement
    : local_variable_declaration
    | assignment_statement
    | return_statement
    | break_statement
    | continue_statement
    | throw_statement
    | expression
    ;

local_variable_declaration
    : type_specifier IDENTIFIER ( ASSIGN expression )?
    ;

assignment_statement
    : lvalue assignment_operator expression
    ;

lvalue
    : IDENTIFIER
    | THIS DOT IDENTIFIER
    | postfixExpression LEFT_SQUARE expression RIGHT_SQUARE
    | postfixExpression DOT IDENTIFIER
    ;

assignment_operator
    : ASSIGN | PLUS_ASSIGN | MINUS_ASSIGN | MULTIPLY_ASSIGN | DIVIDE_ASSIGN | MODULO_ASSIGN
    ;

return_statement
    : RETURN expression?
    ;

break_statement
    : BREAK
    ;

continue_statement
    : CONTINUE
    ;

throw_statement
    : THROW expression
    ;

try_catch_statement
    : TRY
    statement*
    ( CATCH LEFT_PAREN parameter RIGHT_PAREN statement* )+
    ( FINALLY statement+ )?
    ENDTRY
    ;

if_statement
    : IF expression THEN
    statement*
    ( ELSIF expression THEN statement* )*
    ( ELSE statement* )?
    ENDIF
    ;

// TODO: Implement for statement
for_statement
    : FOR /* Placeholder */
    ENDFOR
    ;

// TODO: Implement while statemet
while_statement
    : FOR /* Placeholder */
    ENDWHILE
    ;

expression
    : logicalOrExpression
    ;

logicalOrExpression
    : logicalAndExpression ( OR logicalAndExpression )*
    ;

logicalAndExpression
    : equalityExpression ( AND equalityExpression )*
    ;

equalityExpression
    : relationalExpression ( ( EQUAL | NOT_EQUAL ) relationalExpression )*
    ;

relationalExpression
    : additiveExpression ( ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ) additiveExpression )*
    ;

additiveExpression
    : multiplicativeExpression ( ( PLUS | MINUS ) multiplicativeExpression )*
    ;

multiplicativeExpression
    : unaryExpression ( ( MULTIPLY | DIVIDE | MODULO ) unaryExpression )*
    ;

unaryExpression
    : postfixExpression
    | ( PLUS | MINUS | NOT ) unaryExpression
    ;

postfixExpression
    : primaryExpression
    ( DOT IDENTIFIER // Member access: obj.func()
    | LEFT_SQUARE expression RIGHT_SQUARE // Array access: arr[idx]
    | LEFT_PAREN argument_list? RIGHT_PAREN // Function call: func() or obj.func()
    )* // Allow chaining: obj.func()[idx].field
    ;

primaryExpression
    : literal
    | IDENTIFIER
    | LEFT_PAREN expression RIGHT_PAREN
    | object_creation
    | THIS
    ;

argument_list
    : expression ( COMMA expression )*
    ;

object_creation
    : NEW type_specifier LEFT_PAREN argument_list? RIGHT_PAREN // Object: new MyClass(args)
    | NEW type_specifier LEFT_SQUARE expression RIGHT_SQUARE // Array: new int[size]
    ;

literal
    : INTEGER_LITERAL | FLOAT_LITERAL | DOUBLE_LITERAL |  CHAR_LITERAL | STRING_LITERAL | BOOL_LITERAL | NULL
    ;

// keywords
CLASS : 'class';
ENDCLASS : 'endclass';
FUNCTION : 'function';
ENDFUNCTION : 'endfunction';
CONSTRUCTOR : 'constructor';
ENDCONSTRUCTOR : 'endconstructor';
IF: 'if';
THEN: 'then';
ELSIF: 'elsif';
ELSE: 'else';
ENDIF: 'endif';
FOR: 'for';
ENDFOR: 'endfor';
WHILE: 'while';
ENDWHILE: 'endwhile';
CONTINUE: 'continue';
BREAK: 'break';
TRY: 'try';
CATCH: 'catch';
FINALLY: 'finally';
ENDTRY: 'endtry';
THROW: 'throw';
RETURN: 'return';
EXTENDS: 'extends';
IMPLEMENTS: 'implements';
PUBLIC: 'public';
PRIVATE: 'private';
PROTECTED: 'protected';
CONST: 'const';
STATIC: 'static';
OVERRIDE: 'override';
IMPORT: 'import';
NEW: 'new';
THIS: 'this';
VOID: 'void';
NULL: 'null';

// types
BYTE: 'byte';
BOOL: 'bool';
INT: 'int';
FLOAT: 'float';
DOUBLE: 'double';
CHAR: 'char';
STRING: 'string';
LONG: 'long';

// literals
FLOAT_LITERAL
    : ( [0-9]+  '.' [0-9]* | '.' [0-9]+ ) ( [eE] [+\-]? [0-9]+ )? [fF]
    | [0-9]+ ( [eE] [+\-]? [0-9]+ )? [fF]
    ;
DOUBLE_LITERAL
    : ( [0-9]+ '.' [0-9]* | '.' [0-9]+ ) ( [eE] [+\-]? [0-9]+ )? [dD]?
    | [0-9]+ [eE] [+\-]? [0-9]+ [dD]?
    | [0-9]+ [dD]
    ;
INTEGER_LITERAL: [0-9]+;
CHAR_LITERAL: '\'' ( '\\' [nt\\'"] | ~['\\] ) '\'';
STRING_LITERAL: '"' ( ~["\\] | '\\' . )* '"';
BOOL_LITERAL: 'true' | 'false';

// identifiers
IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]*;

// operators
PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
MODULO: '%';
INCREMENT: '++';
DECREMENT: '--';
ASSIGN: '=';
PLUS_ASSIGN: '+=';
MINUS_ASSIGN: '-=';
MULTIPLY_ASSIGN: '*=';
DIVIDE_ASSIGN: '/=';
MODULO_ASSIGN: '%=';
EQUAL: '==';
NOT_EQUAL: '!=';
LESS: '<';
GREATER: '>';
LESS_EQUAL: '<=';
GREATER_EQUAL: '>=';
AND: 'and';
OR: 'or';
NOT: 'not';


// brackets
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_SQUARE: '[';
RIGHT_SQUARE: ']';

// separators
COMMA: ',';
SEMICOLON: ';';
DOT: '.';
COLON: ':';

// comments
LINE_COMMENT: '#' ~[\r\n]* -> skip;

// whitespace
WS: [ \t\r\n]+ -> skip;
