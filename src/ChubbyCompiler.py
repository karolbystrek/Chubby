from .JavaBytecodeCompiler import JavaBytecodeCompiler
from .JavaBytecodeExecutor import JavaBytecodeExecutor
from antlr.ChubbyParser import ChubbyParser
from antlr.ChubbyVisitor import ChubbyVisitor
from antlr.ChubbyLexer import ChubbyLexer
from antlr4 import *
from antlr4.error.ErrorListener import ErrorListener


class ChubbyCompilerError(Exception):
    """Custom exception for Chubby Compiler errors."""

    def __init__(self, message):
        self.message = message
        super().__init__(self.message)


class ChubbyErrorListener(ErrorListener):
    def __init__(self):
        super().__init__()
        self.errors = []

    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        self.errors.append(f"line {line}:{column} {msg}")


class ChubbyCompiler(ChubbyVisitor):

    def __init__(self):
        self.classes = {}
        self.current_class_name = None
        self.has_scanner = False
        self.has_main_method = False
        self.class_imports = []

    def compile(self, chubby_code: str, ide_input: str = None) -> tuple[bool, str, str]:
        """
        Compiles and executes Chubby code from a string.

        Args:
            chubby_code (str): The ChubbyPython code string.
            ide_input (str, optional): String containing user input for the program. Defaults to None.

        Returns:
            tuple[bool, str, str]: (success, stdout, stderr/compilation_error_message)
        """
        self.classes = {}
        self.current_class_name = None
        self.has_scanner = False
        self.has_main_method = False
        self.class_imports = []

        try:
            input_stream = InputStream(chubby_code)
            lexer = ChubbyLexer(input_stream)
            lexer.removeErrorListeners()
            stream = CommonTokenStream(lexer)
            parser = ChubbyParser(stream)
            parser.removeErrorListeners()
            error_listener = ChubbyErrorListener()
            parser.addErrorListener(error_listener)
            tree = parser.program()

            if error_listener.errors:
                return False, "", "\\n".join(error_listener.errors)

            self.visit(tree)

        except ChubbyCompilerError as e:
            return False, "", f"Compilation error: {e.message}"
        except Exception as e:
            import traceback
            tb_str = traceback.format_exc()
            return False, "", f"An unexpected parsing/compilation error occurred: {e}\n{tb_str}"

        if not self.has_main_method:
            return False, "", "Compilation error: No main method defined"

        bytecode_compiler = JavaBytecodeCompiler()
        success, logs = bytecode_compiler.compile(
            self.classes)
        compilation_logs_str = "\n".join(logs)
        if not success:
            return False, "", f"Compilation failed:\n{compilation_logs_str}"

        executor = JavaBytecodeExecutor()
        stdout, stderr, return_code = executor.execute(list(self.classes.keys()), ide_input=ide_input)
        if return_code == 0:
            return True, stdout, compilation_logs_str
        else:
            return False, stdout, f"Execution error:\n{stderr}\n{compilation_logs_str}"

    def visitProgram(self, ctx: ChubbyParser.ProgramContext) -> None:
        for class_declaration in ctx.class_definition():
            self.visit(class_declaration)

    def visitClass_definition(self, ctx: ChubbyParser.Class_definitionContext) -> None:
        class_name = ctx.IDENTIFIER().getText()
        self.current_class_name = class_name
        self.has_scanner = False
        self.class_imports = []
        self.classes[class_name] = []
        visibility = ""
        if ctx.visibility_modifier():
            visibility = self.visit(ctx.visibility_modifier())
        self.classes[class_name].append(f"{visibility} class {class_name} {{")
        for class_member in ctx.class_member():
            self.visit(class_member)
        self.classes[class_name].append("}")
        self.classes[class_name] = self.class_imports + self.classes[class_name]

    def visitConstructor_definition(
        self, ctx: ChubbyParser.Constructor_definitionContext
    ) -> None:
        visibility = self.visit(ctx.visibility_modifier())
        constructor_name = ctx.IDENTIFIER().getText()
        if constructor_name != self.current_class_name:
            raise ChubbyCompilerError(
                f"Constructor name '{constructor_name}' does not match class name '{self.current_class_name}'"
            )
        parameters = ""
        if ctx.parameter_list():
            parameters = self.visit(ctx.parameter_list())
        self.classes[self.current_class_name].append(
            f"{visibility} {constructor_name}({parameters}) {{"
        )
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

    def visitFunction_definition(
        self, ctx: ChubbyParser.Function_definitionContext
    ) -> None:
        function_name = ctx.IDENTIFIER().getText()
        if function_name == "main":
            if self.has_main_method:
                raise ChubbyCompilerError("Multiple main methods defined")
            self.has_main_method = True
        visibility = ""
        if ctx.visibility_modifier():
            visibility = self.visit(ctx.visibility_modifier()) + " "
        static = ""
        if ctx.STATIC():
            static = "static "
        parameters = ""
        return_type = self.visit(ctx.return_type())
        if ctx.parameter_list():
            parameters = self.visitParameter_list(ctx.parameter_list())
        self.classes[self.current_class_name].append(
            f"{visibility}{static}{return_type} {function_name}({parameters}) {{"
        )
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

    def visitVariable_definition(
        self, ctx: ChubbyParser.Variable_definitionContext
    ) -> None:
        variable_name = ctx.IDENTIFIER().getText()
        visibility = ""
        if ctx.visibility_modifier():
            visibility = self.visit(ctx.visibility_modifier()) + " "
        static = ""
        if ctx.STATIC():
            static = "static "
        final = ""
        if ctx.CONST():
            final = "final "
        variable_type = self.visit(ctx.type_specifier())
        array_suffix = ""
        if ctx.LEFT_SQUARE():
            array_suffix = "[]" * len(ctx.LEFT_SQUARE())
        expression = ""
        if ctx.expression():
            expression = " = " + self.visit(ctx.expression())
        self.classes[self.current_class_name].append(
            f"{visibility}{static}{final}{variable_type}{array_suffix} {variable_name}{expression};"
        )

    def visitStatement(self, ctx: ChubbyParser.StatementContext) -> None:
        if ctx.block_statement():
            self.visit(ctx.block_statement())
        else:
            self.visit(ctx.simple_statement())

    def visitBlock_statement(self, ctx: ChubbyParser.Block_statementContext) -> None:
        if ctx.if_statement():
            self.visit(ctx.if_statement())
        elif ctx.for_statement():
            self.visit(ctx.for_statement())
        else:
            self.visit(ctx.while_statement())

    def visitIf_statement(self, ctx: ChubbyParser.If_statementContext) -> None:
        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"if ({expression}) {{")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")
        if ctx.elsif_statement():
            self.visit(ctx.elsif_statement())
        if ctx.else_statement():
            self.visit(ctx.else_statement())

    def visitElsif_statement(self, ctx: ChubbyParser.Elsif_statementContext) -> None:
        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"else if ({expression}) {{")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")
        if ctx.elsif_statement():
            self.visit(ctx.elsif_statement())

    def visitElse_statement(self, ctx: ChubbyParser.Else_statementContext) -> None:
        self.classes[self.current_class_name].append("else {")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

    def visitFor_statement(self, ctx: ChubbyParser.For_statementContext) -> None:
        for_variable = ctx.IDENTIFIER().getText()
        expression_from = self.visit(ctx.expression(0))
        expression_to = self.visit(ctx.expression(1))
        expression_step = "1"
        if len(ctx.expression()) > 2:
            expression_step = self.visit(ctx.expression(2))
        self.classes[self.current_class_name].append(
            f"for (int {for_variable} = {expression_from}; {for_variable} < {expression_to}; {for_variable} += {expression_step}) {{"
        )
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

    def visitWhile_statement(self, ctx: ChubbyParser.While_statementContext) -> None:
        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"while ({expression}) {{")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

    def visitSimple_statement(self, ctx: ChubbyParser.Simple_statementContext) -> None:
        if ctx.local_variable_declaration():
            self.visit(ctx.local_variable_declaration())
        elif ctx.assignment_statement():
            self.visit(ctx.assignment_statement())
        elif ctx.return_statement():
            self.visit(ctx.return_statement())
        elif ctx.break_statement():
            self.visit(ctx.break_statement())
        elif ctx.continue_statement():
            self.visit(ctx.continue_statement())
        elif ctx.print_statement():
            self.visit(ctx.print_statement())
        elif ctx.expression():
            expression = self.visit(ctx.expression())
            self.classes[self.current_class_name].append(f"{expression};")

    def visitLocal_variable_declaration(
        self, ctx: ChubbyParser.Local_variable_declarationContext
    ) -> None:
        variable_name = ctx.IDENTIFIER().getText()
        variable_type = self.visit(ctx.type_specifier())
        array_suffix = ""
        if ctx.LEFT_SQUARE():
            array_suffix = "[]" * len(ctx.LEFT_SQUARE())
        expression = ""
        if ctx.expression():
            expression = " = " + self.visit(ctx.expression())
        self.classes[self.current_class_name].append(
            f"{variable_type}{array_suffix} {variable_name}{expression};"
        )

    def visitAssignment_statement(
        self, ctx: ChubbyParser.Assignment_statementContext
    ) -> None:
        lvalue = self.visit(ctx.lvalue())
        operator = self.visit(ctx.assignment_operator())
        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(
            f"{lvalue} {operator} {expression};"
        )

    def visitLvalue(self, ctx: ChubbyParser.LvalueContext) -> str:
        if ctx.THIS():
            return f"this.{ctx.IDENTIFIER().getText()}"
        elif ctx.LEFT_SQUARE():
            postfix = self.visit(ctx.postfix_expression())
            expression = self.visit(ctx.expression())
            return f"{postfix}[{expression}]"
        elif ctx.DOT():
            postfix = self.visit(ctx.postfix_expression())
            identifier = ctx.IDENTIFIER().getText()
            return f"{postfix}.{identifier}"
        else:
            return ctx.IDENTIFIER().getText()

    def visitReturn_statement(self, ctx: ChubbyParser.Return_statementContext) -> None:
        expression = ""
        if ctx.expression():
            expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"return {expression};")

    def visitBreak_statement(self, ctx: ChubbyParser.Break_statementContext) -> None:
        self.classes[self.current_class_name].append("break;")

    def visitContinue_statement(
        self, ctx: ChubbyParser.Continue_statementContext
    ) -> None:
        self.classes[self.current_class_name].append("continue;")

    def visitPrint_statement(self, ctx: ChubbyParser.Print_statementContext) -> None:
        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(
            f"System.out.println({expression});"
        )

    def visitExpression(self, ctx: ChubbyParser.ExpressionContext) -> str:
        return self.visit(ctx.logical_expression())

    def visitLogical_expression(
        self, ctx: ChubbyParser.Logical_expressionContext
    ) -> str:
        parts = [self.visit(ctx.equality_expression(0))]
        if ctx.logical_operator():
            for i in range(0, len(ctx.logical_operator())):
                parts.append(self.visit(ctx.logical_operator(i)))
                parts.append(self.visit(ctx.equality_expression(i + 1)))
        return " ".join(parts)

    def visitEquality_expression(
        self, ctx: ChubbyParser.Equality_expressionContext
    ) -> str:
        parts = [self.visit(ctx.relational_expression(0))]
        if ctx.equality_operator():
            for i in range(0, len(ctx.equality_operator())):
                parts.append(self.visit(ctx.equality_operator(i)))
                parts.append(self.visit(ctx.relational_expression(i + 1)))
        return " ".join(parts)

    def visitRelational_expression(
        self, ctx: ChubbyParser.Relational_expressionContext
    ) -> str:
        parts = [self.visit(ctx.additive_expression(0))]
        if ctx.relational_operator():
            for i in range(0, len(ctx.relational_operator())):
                parts.append(self.visit(ctx.relational_operator(i)))
                parts.append(self.visit(ctx.additive_expression(i + 1)))
        return " ".join(parts)

    def visitAdditive_expression(
        self, ctx: ChubbyParser.Additive_expressionContext
    ) -> str:
        parts = [self.visit(ctx.multiplicative_expression(0))]
        if ctx.additive_operator():
            for i in range(0, len(ctx.additive_operator())):
                parts.append(self.visit(ctx.additive_operator(i)))
                parts.append(self.visit(ctx.multiplicative_expression(i + 1)))
        return " ".join(parts)

    def visitMultiplicative_expression(
        self, ctx: ChubbyParser.Multiplicative_expressionContext
    ) -> str:
        parts = [self.visit(ctx.unary_expression(0))]
        if ctx.multiplicative_operator():
            for i in range(0, len(ctx.multiplicative_operator())):
                parts.append(self.visit(ctx.multiplicative_operator(i)))
                parts.append(self.visit(ctx.unary_expression(i + 1)))
        return " ".join(parts)

    def visitUnary_expression(self, ctx: ChubbyParser.Unary_expressionContext) -> str:
        if ctx.postfix_expression():
            return self.visit(ctx.postfix_expression())
        unary_operator = self.visit(ctx.unary_operator())
        return f"{unary_operator}{self.visit(ctx.unary_expression())}"

    def visitPostfix_expression(
        self, ctx: ChubbyParser.Postfix_expressionContext
    ) -> str:
        primary_expression = self.visit(ctx.primary_expression())
        suffix_str = ""
        if ctx.suffix():
            for suffix in ctx.suffix():
                suffix_str += self.visit(suffix)
        return f"{primary_expression}{suffix_str}"

    def visitMember_access_suffix(
        self, ctx: ChubbyParser.Member_access_suffixContext
    ) -> str:
        return f".{ctx.IDENTIFIER().getText()}"

    def visitArray_access_suffix(
        self, ctx: ChubbyParser.Array_access_suffixContext
    ) -> str:
        expression = self.visit(ctx.expression())
        return f"[{expression}]"

    def visitFunction_call_suffix(
        self, ctx: ChubbyParser.Function_call_suffixContext
    ) -> str:
        argument_list = ""
        if ctx.argument_list():
            argument_list = self.visit(ctx.argument_list())
        return f"({argument_list})"

    def visitPrimary_expression(
        self, ctx: ChubbyParser.Primary_expressionContext
    ) -> str:
        if ctx.literal():
            return self.visit(ctx.literal())
        elif ctx.IDENTIFIER():
            return ctx.IDENTIFIER().getText()
        elif ctx.object_creation():
            return self.visit(ctx.object_creation())
        elif ctx.array_creation():
            return self.visit(ctx.array_creation())
        elif ctx.THIS():
            return "this"
        elif ctx.input_statement():
            return self.visit(ctx.input_statement())
        else:
            return f"({self.visit(ctx.expression())})"

    def visitLiteral(self, ctx: ChubbyParser.LiteralContext) -> str:
        return ctx.getText()

    def visitRegular_class_instantiation(self, ctx: ChubbyParser.Regular_class_instantiationContext) -> str:
        base_name_str = ctx.class_name_for_new.text

        type_to_instantiate_chubby = base_name_str

        if ctx.LESS() and ctx.GREATER():
            if ctx.generic_arg_for_new_regular_class:
                generic_arg_type_str = self.visit(ctx.generic_arg_for_new_regular_class)

                java_generic_arg_type = generic_arg_type_str
                if generic_arg_type_str == "int":
                    java_generic_arg_type = "Integer"
                elif generic_arg_type_str == "double":
                    java_generic_arg_type = "Double"
                elif generic_arg_type_str == "boolean":
                    java_generic_arg_type = "Boolean"
                elif generic_arg_type_str == "char":
                    java_generic_arg_type = "Character"
                elif generic_arg_type_str == "long":
                    java_generic_arg_type = "Long"

                type_to_instantiate_chubby = f"{base_name_str}<{java_generic_arg_type}>"
            else:  #
                type_to_instantiate_chubby = f"{base_name_str}<>"

        java_type_to_instantiate_final = type_to_instantiate_chubby

        arguments = ""
        if ctx.argument_list():
            arguments = self.visit(ctx.argument_list())

        return f"new {java_type_to_instantiate_final}({arguments})"

    def visitList_instantiation(self, ctx: ChubbyParser.List_instantiationContext) -> str:
        element_type_str = self.visit(ctx.element_type_for_new_list)

        java_element_type = element_type_str
        if element_type_str == "int":
            java_element_type = "Integer"
        elif element_type_str == "double":
            java_element_type = "Double"
        elif element_type_str == "boolean":
            java_element_type = "Boolean"
        elif element_type_str == "char":
            java_element_type = "Character"
        elif element_type_str == "long":
            java_element_type = "Long"

        if "import java.util.ArrayList;" not in self.class_imports:
            self.class_imports.append("import java.util.ArrayList;")
        if "import java.util.List;" not in self.class_imports:
            self.class_imports.append("import java.util.List;")

        return f"new ArrayList<{java_element_type}>()"


    def visitArgument_list(self, ctx: ChubbyParser.Argument_listContext) -> str:
        argument_list = []
        if ctx.expression():
            for expression in ctx.expression():
                argument_list.append(self.visit(expression))
        return ", ".join(argument_list)

    def visitArray_creation(self, ctx: ChubbyParser.Array_creationContext) -> str:
        if ctx.empty_array_initialization():
            return self.visit(ctx.empty_array_initialization())
        elif ctx.array_initializer_literal():
            return self.visit(ctx.array_initializer_literal())

    def visitArray_initializer_literal(
        self, ctx: ChubbyParser.Array_initializer_literalContext
    ) -> str:
        array_elements = self.visit(ctx.array_elements())
        return f"{{{array_elements}}}"

    def visitArray_elements(self, ctx: ChubbyParser.Array_elementsContext) -> str:
        parts = []
        for array_element in ctx.array_element():
            parts.append(self.visit(array_element))
        return ", ".join(parts)

    def visitArray_element(self, ctx: ChubbyParser.Array_elementContext) -> str:
        if ctx.expression():
            return self.visit(ctx.expression())
        elif ctx.array_initializer_literal():
            return self.visit(ctx.array_initializer_literal())

    def visitInput_statement(self, ctx: ChubbyParser.Input_statementContext) -> str:
        if not self.has_scanner:
            self.class_imports.append("import java.util.Scanner;")
            self.classes[self.current_class_name].insert(
                1, "private static final Scanner scanner = new Scanner(System.in);"
            )
            self.has_scanner = True
        scanner_type = self.visit(ctx.scanner_input_type())
        return f"scanner.next{scanner_type}()"

    def visitScanner_input_type(
        self, ctx: ChubbyParser.Scanner_input_typeContext
    ) -> str:
        if ctx.INT():
            return "Int"
        elif ctx.BOOL():
            return "Boolean"
        elif ctx.LONG():
            return "Long"
        elif ctx.DOUBLE():
            return "Double"
        elif ctx.STRING():
            return ""

    def visitParameter_list(self, ctx: ChubbyParser.Parameter_listContext) -> str:
        parameter_list = []
        if ctx.parameter():
            for parameter in ctx.parameter():
                parameter_list.append(self.visit(parameter))
        return ", ".join(parameter_list)

    def visitParameter(self, ctx: ChubbyParser.ParameterContext) -> str:
        param_type = self.visit(ctx.type_specifier())
        param_name = ctx.IDENTIFIER().getText()
        array_suffix = ""
        if ctx.LEFT_SQUARE():
            array_suffix = "[]" * len(ctx.LEFT_SQUARE())
        return f"{param_type}{array_suffix} {param_name}"

    def visitReturn_type(self, ctx: ChubbyParser.Return_typeContext) -> str:
        if ctx.VOID():
            return "void"
        return_type = self.visit(ctx.type_specifier())
        array_suffix = ""
        if ctx.LEFT_SQUARE():
            array_suffix = "[]" * len(ctx.LEFT_SQUARE())
        return f"{return_type}{array_suffix}"

    def visitType_specifier(self, ctx: ChubbyParser.Type_specifierContext) -> str:
        if ctx.primitive_type():
            return self.visit(ctx.primitive_type())
        elif ctx.list_type_declaration():
            return self.visit(ctx.list_type_declaration())
        elif ctx.generic_identifier_type():
            return self.visit(ctx.generic_identifier_type())
        elif ctx.simple_identifier_type():
            return ctx.simple_identifier_type().IDENTIFIER().getText()
        else:
            raise ChubbyCompilerError(f"Unknown structure in type_specifier: {ctx.getText()}")

    def visitPrimitive_type(self, ctx: ChubbyParser.Primitive_typeContext) -> str:
        if ctx.BOOL(): return "boolean"
        if ctx.INT(): return "int"
        if ctx.DOUBLE(): return "double"
        if ctx.CHAR(): return "char"
        if ctx.STRING(): return "String"
        if ctx.LONG(): return "long"
        return ""

    def visitList_type_declaration(self, ctx: ChubbyParser.List_type_declarationContext) -> str:
        if "import java.util.List;" not in self.class_imports:
            self.class_imports.append("import java.util.List;")

        if ctx.type_arg_in_list_decl:
            element_type_str = self.visit(ctx.type_arg_in_list_decl)
            java_element_type = element_type_str
            if element_type_str == "int":
                java_element_type = "Integer"
            elif element_type_str == "double":
                java_element_type = "Double"
            elif element_type_str == "boolean":
                java_element_type = "Boolean"
            elif element_type_str == "char":
                java_element_type = "Character"
            elif element_type_str == "long":
                java_element_type = "Long"
            return f"List<{java_element_type}>"
        else:
            if "import java.util.ArrayList;" not in self.class_imports:
                self.class_imports.append("import java.util.ArrayList;")
            return "List"

    def visitGeneric_identifier_type(self, ctx: ChubbyParser.Generic_identifier_typeContext) -> str:
        base_name = ctx.IDENTIFIER().getText()
        element_type_str = self.visit(ctx.type_arg_in_ident_decl)

        java_element_type = element_type_str
        if element_type_str == "int": java_element_type = "Integer"
        return f"{base_name}<{java_element_type}>"


    def visitLogical_operator(self, ctx: ChubbyParser.Logical_operatorContext) -> str:
        if ctx.OR():
            return "||"
        elif ctx.AND():
            return "&&"

    def visitEquality_operator(self, ctx: ChubbyParser.Equality_operatorContext) -> str:
        if ctx.EQUAL():
            return "=="
        elif ctx.NOT_EQUAL():
            return "!="

    def visitRelational_operator(
        self, ctx: ChubbyParser.Relational_operatorContext
    ) -> str:
        if ctx.LESS():
            return "<"
        elif ctx.GREATER():
            return ">"
        elif ctx.LESS_EQUAL():
            return "<="
        elif ctx.GREATER_EQUAL():
            return ">="

    def visitAdditive_operator(self, ctx: ChubbyParser.Additive_operatorContext) -> str:
        if ctx.PLUS():
            return "+"
        elif ctx.MINUS():
            return "-"

    def visitMultiplicative_operator(
        self, ctx: ChubbyParser.Multiplicative_operatorContext
    ) -> str:
        if ctx.MULTIPLY():
            return "*"
        elif ctx.DIVIDE():
            return "/"
        elif ctx.MODULO():
            return "%"

    def visitUnary_operator(self, ctx: ChubbyParser.Unary_operatorContext) -> str:
        if ctx.PLUS():
            return "+"
        elif ctx.MINUS():
            return "-"
        elif ctx.NOT():
            return "!"

    def visitAssignment_operator(
        self, ctx: ChubbyParser.Assignment_operatorContext
    ) -> str:
        if ctx.ASSIGN():
            return "="
        elif ctx.PLUS_ASSIGN():
            return "+="
        elif ctx.MINUS_ASSIGN():
            return "-="
        elif ctx.MULTIPLY_ASSIGN():
            return "*="
        elif ctx.DIVIDE_ASSIGN():
            return "/="
        elif ctx.MODULO_ASSIGN():
            return "%="

    def visitVisibility_modifier(
        self, ctx: ChubbyParser.Visibility_modifierContext
    ) -> str:
        if ctx.PUBLIC():
            return "public"
        elif ctx.PRIVATE():
            return "private"
