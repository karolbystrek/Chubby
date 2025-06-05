import traceback
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


class Scope:
    def __init__(self):
        self.variables = {}

    def add_variable(self, name, variable_info):
        if name in self.variables:
            raise ChubbyCompilerError(
                f"Variable '{name}' already defined in this scope {variable_info.get('position', '')}"
            )
        self.variables[name] = variable_info

    def get_variable(self, name):
        return self.variables.get(name)

    def delete_variable(self, name):
        if name in self.variables:
            del self.variables[name]
        else:
            raise ChubbyCompilerError(f"Variable '{name}' not found in this scope")

    def has_variable(self, name) -> bool:
        return name in self.variables


class ChubbyCompiler(ChubbyVisitor):

    def __init__(self):
        self.classes = {}
        self.current_class_name = None
        self.has_scanner = False
        self.has_main_method = False
        self.class_imports = []
        self.scope_stack = []
        self.loop_stack = []
        self.functions = {}
        self.function_context_stack = []

    def get_literal_type(self, literal_ctx) -> str:
        literal_text = literal_ctx.getText()

        if literal_ctx.INTEGER_LITERAL():
            return "int"
        elif literal_ctx.LONG_LITERAL():
            return "long"
        elif literal_ctx.DOUBLE_LITERAL():
            return "double"
        elif literal_ctx.CHAR_LITERAL():
            return "char"
        elif literal_ctx.STRING_LITERAL():
            return "string"
        elif literal_ctx.TRUE() or literal_ctx.FALSE():
            return "bool"
        elif literal_ctx.NULL():
            return "null"
        else:
            raise ChubbyCompilerError(f"Unknown literal type: {literal_text}")

    def normalize_type_name(self, type_name: str) -> str:
        type_mapping = {
            "boolean": "bool",
            "String": "string",
            "Integer": "int",
            "Double": "double",
            "Long": "long",
            "Character": "char",
        }
        return type_mapping.get(type_name, type_name)

    def is_compatible_assignment(self, declared_type: str, literal_type: str) -> bool:
        declared_type = self.normalize_type_name(declared_type)
        literal_type = self.normalize_type_name(literal_type)

        if literal_type == "null":
            return declared_type not in ["int", "long", "double", "char", "bool"]

        if declared_type == literal_type:
            return True

        if declared_type == "long" and literal_type == "int":
            return True
        if declared_type == "double" and literal_type in ["int", "long"]:
            return True
        return False

    def validate_assignment_types(
        self,
        variable_type: str,
        expression_ctx,
        variable_name: str,
        line: int,
        column: int,
    ):
        literal_ctx = self._extract_literal_from_expression(expression_ctx)

        if literal_ctx:
            literal_type = self.get_literal_type(literal_ctx)

            if not self.is_compatible_assignment(variable_type, literal_type):
                raise ChubbyCompilerError(
                    f"Type mismatch at line {line}, column {column}: "
                    f"Cannot assign {literal_type} literal to variable '{variable_name}' of type {variable_type}. "
                    f"Expected {variable_type}, but got {literal_type}."
                )

    def _extract_literal_from_expression(self, expression_ctx):
        if not expression_ctx:
            return None

        if hasattr(expression_ctx, "literal") and expression_ctx.literal():
            return expression_ctx.literal()

        current = expression_ctx

        if hasattr(current, "logical_expression") and current.logical_expression():
            current = current.logical_expression()
            if (
                hasattr(current, "equality_expression")
                and current.equality_expression()
                and len(current.equality_expression()) == 1
            ):
                current = current.equality_expression(0)
                if (
                    hasattr(current, "relational_expression")
                    and current.relational_expression()
                    and len(current.relational_expression()) == 1
                ):
                    current = current.relational_expression(0)
                    if (
                        hasattr(current, "additive_expression")
                        and current.additive_expression()
                        and len(current.additive_expression()) == 1
                    ):
                        current = current.additive_expression(0)
                        if (
                            hasattr(current, "multiplicative_expression")
                            and current.multiplicative_expression()
                            and len(current.multiplicative_expression()) == 1
                        ):
                            current = current.multiplicative_expression(0)
                            if (
                                hasattr(current, "unary_expression")
                                and current.unary_expression()
                                and len(current.unary_expression()) == 1
                            ):
                                current = current.unary_expression(0)
                                if (
                                    hasattr(current, "postfix_expression")
                                    and current.postfix_expression()
                                ):
                                    current = current.postfix_expression()
                                    if (
                                        hasattr(current, "primary_expression")
                                        and current.primary_expression()
                                    ):
                                        current = current.primary_expression()
                                        if (
                                            hasattr(current, "literal")
                                            and current.literal()
                                        ):
                                            return current.literal()

        return None

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
        self.scope_stack = []
        self.loop_stack = []

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
            tb_str = traceback.format_exc()
            return (
                False,
                "",
                f"An unexpected parsing/compilation error occurred: {e}\n{tb_str}",
            )

        bytecode_compiler = JavaBytecodeCompiler()
        success, logs = bytecode_compiler.compile(self.classes)
        compilation_logs_str = "\n".join(logs)
        if not success:
            return False, "", f"Compilation failed:\n{compilation_logs_str}"

        executor = JavaBytecodeExecutor()
        stdout, stderr, return_code = executor.execute(
            list(self.classes.keys()), ide_input=ide_input
        )
        if return_code == 0:
            return True, stdout, compilation_logs_str
        else:
            return False, stdout, f"Execution error:\n{stderr}\n{compilation_logs_str}"

    def visitProgram(self, ctx: ChubbyParser.ProgramContext) -> None:
        for class_declaration in ctx.class_definition():
            self.visit(class_declaration)

        if not self.has_main_method:
            raise ChubbyCompilerError("No main method defined in the program")
        if not self.classes:
            raise ChubbyCompilerError("No classes defined in the program")

    def visitClass_definition(self, ctx: ChubbyParser.Class_definitionContext) -> None:
        self.enter_scope()
        class_name = ctx.IDENTIFIER().getText()
        self.add_to_current_scope(
            class_name, f"at line {ctx.start.line}, column {ctx.start.column}"
        )
        if class_name in self.classes.keys():
            raise ChubbyCompilerError(f"Class '{class_name}' already defined")
        self.current_class_name = class_name
        self.has_scanner = False
        self.class_imports = []
        self.classes[class_name] = []
        visibility = ""
        if ctx.visibility_modifier():
            visibility = self.visit(ctx.visibility_modifier())
        self.classes[class_name].append(f"{visibility} class {class_name} {{")

        self._register_all_functions(ctx)

        for class_member in ctx.class_member():
            self.visit(class_member)
        self.classes[class_name].append("}")
        self.classes[class_name] = self.class_imports + self.classes[class_name]
        self.exit_scope()

    def visitConstructor_definition(
        self, ctx: ChubbyParser.Constructor_definitionContext
    ) -> None:
        self.enter_scope()
        visibility = self.visit(ctx.visibility_modifier())
        constructor_name = ctx.IDENTIFIER().getText()
        if constructor_name != self.current_class_name:
            raise ChubbyCompilerError(
                f"Constructor name '{constructor_name}' does not match class name '{self.current_class_name}'"
            )

        self.function_context_stack.append(
            {
                "name": constructor_name,
                "return_type": "void",
                "position": f"at line {ctx.start.line}, column {ctx.start.column}",
            }
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

        self.function_context_stack.pop()
        self.exit_scope()

    def visitFunction_definition(
        self, ctx: ChubbyParser.Function_definitionContext
    ) -> None:
        self.enter_scope()
        function_name = ctx.IDENTIFIER().getText()
        visibility = ""
        if ctx.visibility_modifier():
            visibility = self.visit(ctx.visibility_modifier()) + " "
        static = ""
        if ctx.STATIC():
            static = "static "

        function_key = f"{self.current_class_name}.{function_name}"
        function_info = self.functions[function_key]
        return_type = function_info["return_type"]

        self.function_context_stack.append(
            {
                "name": function_name,
                "return_type": return_type,
                "position": f"at line {ctx.start.line}, column {ctx.start.column}",
            }
        )

        parameters = ""
        if ctx.parameter_list():
            parameters = self.visitParameter_list(ctx.parameter_list())
        self.classes[self.current_class_name].append(
            f"{visibility}{static}{return_type} {function_name}({parameters}) {{"
        )
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

        self.function_context_stack.pop()
        self.exit_scope()

    def visitVariable_definition(
        self, ctx: ChubbyParser.Variable_definitionContext
    ) -> None:
        variable_name = ctx.IDENTIFIER().getText()
        variable_type = self.visit(ctx.type_specifier())
        is_array = bool(ctx.LEFT_SQUARE())

        self.add_to_current_scope(
            variable_name,
            f"at line {ctx.start.line}, column {ctx.start.column}",
            variable_type,
            is_array,
        )

        visibility = ""
        if ctx.visibility_modifier():
            visibility = self.visit(ctx.visibility_modifier()) + " "
        static = ""
        if ctx.STATIC():
            static = "static "
        final = ""
        if ctx.CONST():
            final = "final "

        array_suffix = ""
        if ctx.LEFT_SQUARE():
            array_suffix = "[]" * len(ctx.LEFT_SQUARE())

        expression = ""
        if ctx.expression():
            self.validate_assignment_types(
                variable_type + array_suffix,
                ctx.expression(),
                variable_name,
                ctx.start.line,
                ctx.start.column,
            )
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
        self.enter_scope()
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
        self.exit_scope()

    def visitElsif_statement(self, ctx: ChubbyParser.Elsif_statementContext) -> None:
        self.enter_scope()
        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"else if ({expression}) {{")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")
        if ctx.elsif_statement():
            self.visit(ctx.elsif_statement())
        self.exit_scope()

    def visitElse_statement(self, ctx: ChubbyParser.Else_statementContext) -> None:
        self.enter_scope()
        self.classes[self.current_class_name].append("else {")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")
        self.exit_scope()

    def visitFor_statement(self, ctx: ChubbyParser.For_statementContext) -> None:
        self.enter_scope()
        self.enter_loop_context("for", ctx.start.line, ctx.start.column)

        for_variable = ctx.IDENTIFIER().getText()
        self.add_to_current_scope(
            for_variable,
            f"at line {ctx.start.line}, column {ctx.start.column}",
            "int",  # for loop variables are always int
        )
        expression_from = self.visit(ctx.expression(0))
        expression_to = self.visit(ctx.expression(1))
        expression_step = "1"
        if len(ctx.expression()) > 2:
            expression_step = self.visit(ctx.expression(2))
        self.classes[self.current_class_name].append(
            f"for (int {for_variable} = {expression_from}; {for_variable} < {expression_to}; {for_variable} += {for_variable} += {expression_step}) {{"
        )
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

        self.exit_loop_context()
        self.exit_scope()

    def visitWhile_statement(self, ctx: ChubbyParser.While_statementContext) -> None:
        self.enter_scope()
        self.enter_loop_context("while", ctx.start.line, ctx.start.column)

        expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"while ({expression}) {{")
        if ctx.statement():
            for statement in ctx.statement():
                self.visit(statement)
        self.classes[self.current_class_name].append("}")

        self.exit_loop_context()
        self.exit_scope()

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
        is_array = bool(ctx.LEFT_SQUARE())

        self.add_to_current_scope(
            variable_name,
            f"at line {ctx.start.line}, column {ctx.start.column}",
            variable_type,
            is_array,
        )

        array_suffix = ""
        if ctx.LEFT_SQUARE():
            array_suffix = "[]" * len(ctx.LEFT_SQUARE())

        expression = ""
        if ctx.expression():
            self.validate_assignment_types(
                variable_type + array_suffix,
                ctx.expression(),
                variable_name,
                ctx.start.line,
                ctx.start.column,
            )
            expression = " = " + self.visit(ctx.expression())

        self.classes[self.current_class_name].append(
            f"{variable_type}{array_suffix} {variable_name}{expression};"
        )

    def visitAssignment_statement(
        self, ctx: ChubbyParser.Assignment_statementContext
    ) -> None:
        lvalue = self.visit(ctx.lvalue())
        operator = self.visit(ctx.assignment_operator())

        if (
            ctx.lvalue().IDENTIFIER()
            and not ctx.lvalue().LEFT_SQUARE()
            and not ctx.lvalue().DOT()
        ):
            variable_name = ctx.lvalue().IDENTIFIER().getText()
            variable_type = self.get_variable_type_from_scope(variable_name)

            if (
                variable_type and operator == "="
            ):  # Only check direct assignments for now
                self.validate_assignment_types(
                    variable_type,
                    ctx.expression(),
                    variable_name,
                    ctx.start.line,
                    ctx.start.column,
                )

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
        self.validate_return_type(ctx.expression(), ctx.start.line, ctx.start.column)

        expression = ""
        if ctx.expression():
            expression = self.visit(ctx.expression())
        self.classes[self.current_class_name].append(f"return {expression};")

    def visitBreak_statement(self, ctx: ChubbyParser.Break_statementContext) -> None:
        self.validate_break_continue_context("break", ctx.start.line, ctx.start.column)
        self.classes[self.current_class_name].append("break;")

    def visitContinue_statement(
        self, ctx: ChubbyParser.Continue_statementContext
    ) -> None:
        self.validate_break_continue_context(
            "continue", ctx.start.line, ctx.start.column
        )
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
            for i, suffix in enumerate(ctx.suffix()):
                if suffix.function_call_suffix():
                    function_name = None

                    if ctx.primary_expression().IDENTIFIER() and i == 0:
                        function_name = ctx.primary_expression().IDENTIFIER().getText()
                    elif i > 0 and ctx.suffix(i - 1).member_access_suffix():
                        function_name = (
                            ctx.suffix(i - 1)
                            .member_access_suffix()
                            .IDENTIFIER()
                            .getText()
                        )

                    if function_name:
                        argument_count = self.get_argument_count_from_context(
                            suffix.function_call_suffix().argument_list()
                        )
                        self.validate_function_call(
                            function_name,
                            argument_count,
                            suffix.start.line,
                            suffix.start.column,
                        )

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

    def visitRegular_class_instantiation(
        self, ctx: ChubbyParser.Regular_class_instantiationContext
    ) -> str:
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

    def visitList_instantiation(
        self, ctx: ChubbyParser.List_instantiationContext
    ) -> str:
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
        is_array = bool(ctx.LEFT_SQUARE())

        self.add_to_current_scope(
            param_name,
            f"at line {ctx.start.line}, column {ctx.start.column}",
            param_type,
            is_array,
        )

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
            raise ChubbyCompilerError(
                f"Unknown structure in type_specifier: {ctx.getText()}"
            )

    def visitPrimitive_type(self, ctx: ChubbyParser.Primitive_typeContext) -> str:
        if ctx.BOOL():
            return "boolean"
        if ctx.INT():
            return "int"
        if ctx.DOUBLE():
            return "double"
        if ctx.CHAR():
            return "char"
        if ctx.STRING():
            return "String"
        if ctx.LONG():
            return "long"
        return ""

    def visitList_type_declaration(
        self, ctx: ChubbyParser.List_type_declarationContext
    ) -> str:
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

    def visitGeneric_identifier_type(
        self, ctx: ChubbyParser.Generic_identifier_typeContext
    ) -> str:
        base_name = ctx.IDENTIFIER().getText()
        element_type_str = self.visit(ctx.type_arg_in_ident_decl)

        java_element_type = element_type_str
        if element_type_str == "int":
            java_element_type = "Integer"
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

    def enter_scope(self):
        self.scope_stack.append(Scope())

    def exit_scope(self):
        if self.scope_stack:
            self.scope_stack.pop()

    def get_current_scope(self):
        if not self.scope_stack:
            raise ChubbyCompilerError("No current scope available")
        return self.scope_stack[-1]

    def get_variable_type_from_scope(self, variable_name: str) -> str:
        for scope in reversed(self.scope_stack):
            if scope.has_variable(variable_name):
                var_info = scope.get_variable(variable_name)
                return var_info.get("type")
        return None

    def add_to_current_scope(
        self, name: str, info: str, var_type: str = None, is_array: bool = False
    ):
        if not self.scope_stack:
            raise ChubbyCompilerError("No current scope available to add variable")

        for scope in reversed(self.scope_stack):
            if scope.has_variable(name):
                existing_info = scope.variables[name]
                raise ChubbyCompilerError(
                    f"Variable '{name}' already defined in an accessible scope. "
                    f"Previously defined {existing_info.get('position', '')}; current declaration {info}"
                )

        variable_info = {"position": info, "type": var_type, "is_array": is_array}
        self.get_current_scope().add_variable(name, variable_info)

    def delete_from_current_scope(self, name: str):
        if not self.scope_stack:
            raise ChubbyCompilerError("No current scope available to delete variable")
        self.get_current_scope().delete_variable(name)

    def enter_loop_context(self, loop_type: str, line: int, column: int):
        self.loop_stack.append({"type": loop_type, "line": line, "column": column})

    def exit_loop_context(self):
        if self.loop_stack:
            self.loop_stack.pop()

    def is_inside_loop(self) -> bool:
        return len(self.loop_stack) > 0

    def validate_break_continue_context(
        self, statement_type: str, line: int, column: int
    ):
        if not self.is_inside_loop():
            keyword = "stop" if statement_type == "break" else "next"
            raise ChubbyCompilerError(
                f"'{keyword}' statement at line {line}, column {column} is not allowed outside of a loop. "
                f"'{keyword}' statements can only be used inside 'for' or 'while' loops."
            )

    def get_argument_count_from_context(self, argument_list_ctx):
        """
        Count the number of arguments in a function call from the argument_list context.

        Args:
            argument_list_ctx: The argument_list context from the parser, or None if no arguments

        Returns:
            int: The number of arguments in the function call
        """
        if argument_list_ctx is None:
            return 0

        # Count the number of expression nodes in the argument list
        expressions = argument_list_ctx.expression()
        if expressions is None:
            return 0

        return len(expressions)

    def validate_function_call(
        self, function_name: str, argument_count: int, line: int, column: int
    ):
        function_found = False
        matching_function = None

        function_key = f"{self.current_class_name}.{function_name}"
        if function_key in self.functions:
            function_found = True
            matching_function = self.functions[function_key]
        else:
            for key, func_info in self.functions.items():
                if func_info["name"] == function_name and func_info["is_static"]:
                    function_found = True
                    matching_function = func_info
                    break

        if not function_found:
            raise ChubbyCompilerError(
                f"Function '{function_name}' is not defined at line {line}, column {column}"
            )

        expected_params = matching_function["parameter_count"]
        if argument_count != expected_params:
            raise ChubbyCompilerError(
                f"Function '{function_name}' expects {expected_params} argument(s), "
                f"but {argument_count} were provided at line {line}, column {column}. "
                f"Function defined {matching_function['position']}"
            )

    def get_expression_type(self, expression_ctx):
        if not expression_ctx:
            return None

        literal_ctx = self._extract_literal_from_expression(expression_ctx)
        if literal_ctx:
            return self.get_literal_type(literal_ctx)

        if (
            hasattr(expression_ctx, "logical_expression")
            and expression_ctx.logical_expression()
        ):
            logical_expr = expression_ctx.logical_expression()
            if logical_expr.logical_operator():
                return "bool"

            if (
                hasattr(logical_expr, "equality_expression")
                and logical_expr.equality_expression()
                and len(logical_expr.equality_expression()) == 1
            ):

                eq_expr = logical_expr.equality_expression(0)
                if eq_expr.equality_operator():
                    return "bool"

                if (
                    hasattr(eq_expr, "relational_expression")
                    and eq_expr.relational_expression()
                    and len(eq_expr.relational_expression()) == 1
                ):

                    rel_expr = eq_expr.relational_expression(0)
                    if rel_expr.relational_operator():
                        return "bool"

                    if (
                        hasattr(rel_expr, "additive_expression")
                        and rel_expr.additive_expression()
                        and len(rel_expr.additive_expression()) >= 1
                    ):

                        if len(rel_expr.additive_expression()) > 1:
                            return self._get_arithmetic_result_type(
                                rel_expr.additive_expression()
                            )

                        add_expr = rel_expr.additive_expression(0)
                        if (
                            hasattr(add_expr, "multiplicative_expression")
                            and add_expr.multiplicative_expression()
                            and len(add_expr.multiplicative_expression()) >= 1
                        ):

                            if len(add_expr.multiplicative_expression()) > 1:
                                return self._get_arithmetic_result_type(
                                    add_expr.multiplicative_expression()
                                )

                            mult_expr = add_expr.multiplicative_expression(0)
                            if (
                                hasattr(mult_expr, "unary_expression")
                                and mult_expr.unary_expression()
                                and len(mult_expr.unary_expression()) >= 1
                            ):

                                unary_expr = mult_expr.unary_expression(0)
                                if (
                                    hasattr(unary_expr, "postfix_expression")
                                    and unary_expr.postfix_expression()
                                ):

                                    postfix_expr = unary_expr.postfix_expression()
                                    if (
                                        hasattr(postfix_expr, "primary_expression")
                                        and postfix_expr.primary_expression()
                                    ):

                                        primary_expr = postfix_expr.primary_expression()

                                        if (
                                            hasattr(postfix_expr, "suffix")
                                            and postfix_expr.suffix()
                                        ):
                                            for i, suffix in enumerate(
                                                postfix_expr.suffix()
                                            ):
                                                if suffix.function_call_suffix():
                                                    function_name = None

                                                    if (
                                                        primary_expr.IDENTIFIER()
                                                        and i == 0
                                                    ):
                                                        function_name = (
                                                            primary_expr.IDENTIFIER().getText()
                                                        )
                                                    elif (
                                                        i > 0
                                                        and postfix_expr.suffix(
                                                            i - 1
                                                        ).member_access_suffix()
                                                    ):
                                                        function_name = (
                                                            postfix_expr.suffix(i - 1)
                                                            .member_access_suffix()
                                                            .IDENTIFIER()
                                                            .getText()
                                                        )

                                                    if function_name:
                                                        function_key = f"{self.current_class_name}.{function_name}"
                                                        if (
                                                            function_key
                                                            in self.functions
                                                        ):
                                                            return self.functions[
                                                                function_key
                                                            ]["return_type"]
                                                        else:
                                                            for (
                                                                key,
                                                                func_info,
                                                            ) in self.functions.items():
                                                                if (
                                                                    func_info["name"]
                                                                    == function_name
                                                                    and func_info[
                                                                        "is_static"
                                                                    ]
                                                                ):
                                                                    return func_info[
                                                                        "return_type"
                                                                    ]

                                        if (
                                            hasattr(primary_expr, "IDENTIFIER")
                                            and primary_expr.IDENTIFIER()
                                        ):
                                            var_name = (
                                                primary_expr.IDENTIFIER().getText()
                                            )
                                            return self.get_variable_type_from_scope(
                                                var_name
                                            )
                                        elif (
                                            hasattr(primary_expr, "input_statement")
                                            and primary_expr.input_statement()
                                        ):
                                            scanner_type = (
                                                primary_expr.input_statement().scanner_input_type()
                                            )
                                            return self._get_scanner_type(scanner_type)

        return None

    def _get_arithmetic_result_type(self, expressions):
        has_double = False
        has_long = False
        has_string = False

        for expr in expressions:
            expr_type = self.get_expression_type(expr)
            if expr_type == "double":
                has_double = True
            elif expr_type == "long":
                has_long = True
            elif expr_type == "string":
                has_string = True

        if has_string:
            return "string"
        elif has_double:
            return "double"
        elif has_long:
            return "long"
        else:
            return "int"

    def _get_scanner_type(self, scanner_type_ctx):
        if scanner_type_ctx.INT():
            return "int"
        elif scanner_type_ctx.BOOL():
            return "bool"
        elif scanner_type_ctx.LONG():
            return "long"
        elif scanner_type_ctx.DOUBLE():
            return "double"
        elif scanner_type_ctx.STRING():
            return "string"
        return None

    def is_compatible_return_type(
        self, declared_type: str, expression_type: str
    ) -> bool:
        if not expression_type:
            return declared_type == "void"

        declared_type = self.normalize_type_name(declared_type)
        expression_type = self.normalize_type_name(expression_type)

        if declared_type == "void":
            return False

        if expression_type == "null":
            return declared_type not in ["int", "long", "double", "char", "bool"]

        if declared_type == expression_type:
            return True

        if declared_type == "long" and expression_type == "int":
            return True
        if declared_type == "double" and expression_type in ["int", "long"]:
            return True

        return False

    def validate_return_type(self, expression_ctx, line: int, column: int):
        if not self.function_context_stack:
            raise ChubbyCompilerError(
                f"Return statement at line {line}, column {column} is not inside a function"
            )

        current_function = self.function_context_stack[-1]
        expected_return_type = current_function["return_type"]

        if not expression_ctx and expected_return_type != "void":
            raise ChubbyCompilerError(
                f"Return statement at line {line}, column {column} missing expression. "
                f"Function '{current_function['name']}' expects return type '{expected_return_type}'"
            )

        if expression_ctx and expected_return_type == "void":
            raise ChubbyCompilerError(
                f"Return statement at line {line}, column {column} should not have expression. "
                f"Function '{current_function['name']}' is declared as void"
            )

        if expression_ctx:
            expression_type = self.get_expression_type(expression_ctx)

            if not self.is_compatible_return_type(
                expected_return_type, expression_type
            ):
                raise ChubbyCompilerError(
                    f"Return type mismatch at line {line}, column {column}: "
                    f"Function '{current_function['name']}' expects return type '{expected_return_type}', "
                    f"but got '{expression_type}'"
                )

    def _register_all_functions(
        self, ctx: ChubbyParser.Class_definitionContext
    ) -> None:
        for class_member in ctx.class_member():
            if class_member.function_definition():
                self._register_function_signature(class_member.function_definition())

    def _register_function_signature(
        self, ctx: ChubbyParser.Function_definitionContext
    ) -> None:
        function_name = ctx.IDENTIFIER().getText()

        if function_name == "main" and ctx.STATIC():
            if self.has_main_method:
                raise ChubbyCompilerError("Multiple main methods defined")
            self.has_main_method = True

        parameter_count = 0
        if ctx.parameter_list():
            parameter_count = len(ctx.parameter_list().parameter())

        function_key = f"{self.current_class_name}.{function_name}"
        if function_key in self.functions:
            existing_func = self.functions[function_key]
            raise ChubbyCompilerError(
                f"Function '{function_name}' already defined in class '{self.current_class_name}'. "
                f"Previously defined {existing_func['position']}"
            )

        return_type = self.visit(ctx.return_type())

        self.functions[function_key] = {
            "name": function_name,
            "class": self.current_class_name,
            "parameter_count": parameter_count,
            "position": f"at line {ctx.start.line}, column {ctx.start.column}",
            "is_static": bool(ctx.STATIC()),
            "return_type": return_type,
        }
