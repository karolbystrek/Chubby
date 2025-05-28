grammar Chubby;

program: class_definition+ EOF;

class_definition:
	CLASS visibility_modifier? IDENTIFIER class_member* ENDCLASS;

class_member:
	function_definition
	| constructor_definition
	| variable_definition;

constructor_definition:
	CONSTRUCTOR visibility_modifier? IDENTIFIER LEFT_PAREN parameter_list? RIGHT_PAREN statement*
		ENDCONSTRUCTOR;

variable_definition:
	visibility_modifier? STATIC? CONST? type_specifier (
		LEFT_SQUARE RIGHT_SQUARE
	)* IDENTIFIER (ASSIGN expression)? SEMICOLON;

function_definition:
	FUNCTION visibility_modifier? STATIC? IDENTIFIER LEFT_PAREN parameter_list? RIGHT_PAREN COLON
		return_type statement* ENDFUNCTION;

visibility_modifier: PUBLIC | PRIVATE;

parameter_list: parameter (COMMA parameter)*;

parameter:
	type_specifier (LEFT_SQUARE RIGHT_SQUARE)* IDENTIFIER;


type_specifier:
    primitive_type
    | list_type_declaration
    | generic_identifier_type
    | simple_identifier_type
    ;

primitive_type: BOOL | INT | DOUBLE | CHAR | STRING | LONG;

list_type_declaration:
    LIST (LESS type_arg_in_list_decl=type_specifier GREATER)?
    ;

generic_identifier_type:
    IDENTIFIER LESS type_arg_in_ident_decl=type_specifier GREATER
    ;

simple_identifier_type:
    IDENTIFIER
    ;

return_type: type_specifier (LEFT_SQUARE RIGHT_SQUARE)* | VOID;

statement: block_statement | simple_statement SEMICOLON;

block_statement: if_statement | for_statement | while_statement;

simple_statement:
	local_variable_declaration
	| assignment_statement
	| return_statement
	| break_statement
	| continue_statement
	| print_statement
	| expression;

local_variable_declaration:
	type_specifier (LEFT_SQUARE RIGHT_SQUARE)* IDENTIFIER (
		ASSIGN expression
	)?;

assignment_statement: lvalue assignment_operator expression;

lvalue:
	IDENTIFIER
	| THIS DOT IDENTIFIER
	| postfix_expression LEFT_SQUARE expression RIGHT_SQUARE
	| postfix_expression DOT IDENTIFIER;

assignment_operator:
	ASSIGN
	| PLUS_ASSIGN
	| MINUS_ASSIGN
	| MULTIPLY_ASSIGN
	| DIVIDE_ASSIGN
	| MODULO_ASSIGN;

return_statement: RETURN expression?;

break_statement: BREAK;

continue_statement: CONTINUE;

print_statement: PRINT LEFT_PAREN expression RIGHT_PAREN;

if_statement:
	IF expression THEN statement* elsif_statement? else_statement? ENDIF;

elsif_statement:
	ELSIF expression THEN statement* elsif_statement?;

else_statement: ELSE statement*;

for_statement:
	FOR IDENTIFIER IN expression RANGE expression (BY expression)? DO statement* ENDFOR;

while_statement: WHILE expression DO statement* ENDWHILE;

expression: logical_expression;

logical_expression:
	equality_expression (logical_operator equality_expression)*;

equality_expression:
	relational_expression (
		equality_operator relational_expression
	)*;

relational_expression:
	additive_expression (relational_operator additive_expression)*;

additive_expression:
	multiplicative_expression (
		additive_operator multiplicative_expression
	)*;

multiplicative_expression:
	unary_expression (multiplicative_operator unary_expression)*;

unary_expression:
	unary_operator unary_expression
	| postfix_expression;

postfix_expression: primary_expression (suffix)*;

suffix:
	member_access_suffix
	| array_access_suffix
	| function_call_suffix;

member_access_suffix: DOT IDENTIFIER;

array_access_suffix: LEFT_SQUARE expression RIGHT_SQUARE;

function_call_suffix: LEFT_PAREN argument_list? RIGHT_PAREN;

primary_expression:
	literal
	| IDENTIFIER
	| LEFT_PAREN expression RIGHT_PAREN
	| object_creation
	| array_creation
	| input_statement
	| THIS;

argument_list: expression ( COMMA expression)*;

object_creation:
    NEW
    (
        list_instantiation
        |
        regular_class_instantiation
    )
    ;

list_instantiation:
    LIST LEFT_PAREN element_type_for_new_list=type_specifier RIGHT_PAREN
    ;

regular_class_instantiation:
    class_name_for_new=IDENTIFIER
    (LESS generic_arg_for_new_regular_class=type_specifier? GREATER)?
    LEFT_PAREN argument_list? RIGHT_PAREN
    ;

array_creation:
	empty_array_initialization
	| array_initializer_literal;

empty_array_initialization:
	NEW type_specifier (LEFT_SQUARE expression RIGHT_SQUARE)+;

array_initializer_literal:
	LEFT_CURLY array_elements RIGHT_CURLY;

array_elements: array_element (COMMA array_element)*;

array_element: expression | array_initializer_literal;

input_statement:
	INPUT LEFT_PAREN scanner_input_type RIGHT_PAREN;

scanner_input_type: INT | BOOL | LONG | DOUBLE | STRING;

logical_operator: OR | AND;

equality_operator: EQUAL | NOT_EQUAL;

relational_operator:
	LESS
	| GREATER
	| LESS_EQUAL
	| GREATER_EQUAL;

additive_operator: PLUS | MINUS;

multiplicative_operator: MULTIPLY | DIVIDE | MODULO;

unary_operator: PLUS | MINUS | NOT;

literal:
	INTEGER_LITERAL
	| LONG_LITERAL
	| DOUBLE_LITERAL
	| CHAR_LITERAL
	| STRING_LITERAL
	| TRUE
	| FALSE
	| NULL;

CLASS: 'class';
ENDCLASS: 'endclass';
FUNCTION: 'function';
ENDFUNCTION: 'endfunction';
CONSTRUCTOR: 'constructor';
ENDCONSTRUCTOR: 'endconstructor';
IF: 'if';
THEN: 'then';
ELSIF: 'elsif';
ELSE: 'else';
ENDIF: 'endif';
DO: 'do';
FOR: 'for';
IN: 'in';
RANGE: '..';
BY: 'by';
ENDFOR: 'endfor';
WHILE: 'while';
ENDWHILE: 'endwhile';
CONTINUE: 'next';
BREAK: 'stop';
RETURN: 'return';
PUBLIC: 'public';
PRIVATE: 'private';
CONST: 'const';
STATIC: 'static';
OVERRIDE: 'override';
NEW: 'new';
THIS: 'this';
VOID: 'void';
NULL: 'null';
TRUE: 'true';
FALSE: 'false';
PRINT: 'print';
INPUT: 'input';
LIST: 'List';

BOOL: 'bool';
CHAR: 'char';
INT: 'int';
LONG: 'long';
DOUBLE: 'double';
STRING: 'string';

AND: 'and';
OR: 'or';
NOT: 'not';

PLUS_ASSIGN: '+=';
MINUS_ASSIGN: '-=';
MULTIPLY_ASSIGN: '*=';
DIVIDE_ASSIGN: '/=';
MODULO_ASSIGN: '%=';
EQUAL: '==';
NOT_EQUAL: '!=';
LESS_EQUAL: '<=';
GREATER_EQUAL: '>=';

PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
MODULO: '%';
ASSIGN: '=';
LESS: '<';
GREATER: '>';

LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_SQUARE: '[';
RIGHT_SQUARE: ']';
LEFT_CURLY: '{';
RIGHT_CURLY: '}';

COMMA: ',';
SEMICOLON: ';';
DOT: '.';
COLON: ':';

LONG_LITERAL: [0-9]+ [lL];
DOUBLE_LITERAL: ([0-9]+ '.' [0-9]* | '.' [0-9]+) (
		[eE] [+\-]? [0-9]+
	)? [dD]?
	| [0-9]+ [eE] [+\-]? [0-9]+ [dD]?
	| [0-9]+ [dD];
INTEGER_LITERAL: [0-9]+;
CHAR_LITERAL: '\'' ( '\\' [nt\\'"] | ~['\\]) '\'';
STRING_LITERAL: '"' ( ~["\\] | '\\' .)* '"';

IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]*;

LINE_COMMENT: '#' ~[\r\n]* -> skip;

WS: [ \t\r\n]+ -> skip;
