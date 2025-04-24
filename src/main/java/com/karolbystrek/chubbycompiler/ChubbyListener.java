// Generated from C:/Users/chame/Desktop/sem4/kompilatory/OwnProgrammingLanguage/src/main/antlr4/com/karolbystrek/chubbycompiler/Chubby.g4 by ANTLR 4.13.2
package com.karolbystrek.chubbycompiler;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ChubbyParser}.
 */
public interface ChubbyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ChubbyParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ChubbyParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#import_statement}.
	 * @param ctx the parse tree
	 */
	void enterImport_statement(ChubbyParser.Import_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#import_statement}.
	 * @param ctx the parse tree
	 */
	void exitImport_statement(ChubbyParser.Import_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#qualified_identifier}.
	 * @param ctx the parse tree
	 */
	void enterQualified_identifier(ChubbyParser.Qualified_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#qualified_identifier}.
	 * @param ctx the parse tree
	 */
	void exitQualified_identifier(ChubbyParser.Qualified_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#class_definition}.
	 * @param ctx the parse tree
	 */
	void enterClass_definition(ChubbyParser.Class_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#class_definition}.
	 * @param ctx the parse tree
	 */
	void exitClass_definition(ChubbyParser.Class_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#class_body}.
	 * @param ctx the parse tree
	 */
	void enterClass_body(ChubbyParser.Class_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#class_body}.
	 * @param ctx the parse tree
	 */
	void exitClass_body(ChubbyParser.Class_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#class_member}.
	 * @param ctx the parse tree
	 */
	void enterClass_member(ChubbyParser.Class_memberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#class_member}.
	 * @param ctx the parse tree
	 */
	void exitClass_member(ChubbyParser.Class_memberContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#constructor_definition}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_definition(ChubbyParser.Constructor_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#constructor_definition}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_definition(ChubbyParser.Constructor_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#constructor_body}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_body(ChubbyParser.Constructor_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#constructor_body}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_body(ChubbyParser.Constructor_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#variable_definition}.
	 * @param ctx the parse tree
	 */
	void enterVariable_definition(ChubbyParser.Variable_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#variable_definition}.
	 * @param ctx the parse tree
	 */
	void exitVariable_definition(ChubbyParser.Variable_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void enterFunction_definition(ChubbyParser.Function_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void exitFunction_definition(ChubbyParser.Function_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#visibility_modifier}.
	 * @param ctx the parse tree
	 */
	void enterVisibility_modifier(ChubbyParser.Visibility_modifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#visibility_modifier}.
	 * @param ctx the parse tree
	 */
	void exitVisibility_modifier(ChubbyParser.Visibility_modifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#parameter_list}.
	 * @param ctx the parse tree
	 */
	void enterParameter_list(ChubbyParser.Parameter_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#parameter_list}.
	 * @param ctx the parse tree
	 */
	void exitParameter_list(ChubbyParser.Parameter_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(ChubbyParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(ChubbyParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void enterType_specifier(ChubbyParser.Type_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void exitType_specifier(ChubbyParser.Type_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#return_type}.
	 * @param ctx the parse tree
	 */
	void enterReturn_type(ChubbyParser.Return_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#return_type}.
	 * @param ctx the parse tree
	 */
	void exitReturn_type(ChubbyParser.Return_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#function_body}.
	 * @param ctx the parse tree
	 */
	void enterFunction_body(ChubbyParser.Function_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#function_body}.
	 * @param ctx the parse tree
	 */
	void exitFunction_body(ChubbyParser.Function_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ChubbyParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ChubbyParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#block_statement}.
	 * @param ctx the parse tree
	 */
	void enterBlock_statement(ChubbyParser.Block_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#block_statement}.
	 * @param ctx the parse tree
	 */
	void exitBlock_statement(ChubbyParser.Block_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#simple_statement}.
	 * @param ctx the parse tree
	 */
	void enterSimple_statement(ChubbyParser.Simple_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#simple_statement}.
	 * @param ctx the parse tree
	 */
	void exitSimple_statement(ChubbyParser.Simple_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#local_variable_declaration}.
	 * @param ctx the parse tree
	 */
	void enterLocal_variable_declaration(ChubbyParser.Local_variable_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#local_variable_declaration}.
	 * @param ctx the parse tree
	 */
	void exitLocal_variable_declaration(ChubbyParser.Local_variable_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_statement(ChubbyParser.Assignment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#assignment_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_statement(ChubbyParser.Assignment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterLvalue(ChubbyParser.LvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitLvalue(ChubbyParser.LvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#assignment_operator}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_operator(ChubbyParser.Assignment_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#assignment_operator}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_operator(ChubbyParser.Assignment_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(ChubbyParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(ChubbyParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak_statement(ChubbyParser.Break_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak_statement(ChubbyParser.Break_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue_statement(ChubbyParser.Continue_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue_statement(ChubbyParser.Continue_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#throw_statement}.
	 * @param ctx the parse tree
	 */
	void enterThrow_statement(ChubbyParser.Throw_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#throw_statement}.
	 * @param ctx the parse tree
	 */
	void exitThrow_statement(ChubbyParser.Throw_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#try_catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterTry_catch_statement(ChubbyParser.Try_catch_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#try_catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitTry_catch_statement(ChubbyParser.Try_catch_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(ChubbyParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(ChubbyParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#for_statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_statement(ChubbyParser.For_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#for_statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_statement(ChubbyParser.For_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#for_init}.
	 * @param ctx the parse tree
	 */
	void enterFor_init(ChubbyParser.For_initContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#for_init}.
	 * @param ctx the parse tree
	 */
	void exitFor_init(ChubbyParser.For_initContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#for_update}.
	 * @param ctx the parse tree
	 */
	void enterFor_update(ChubbyParser.For_updateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#for_update}.
	 * @param ctx the parse tree
	 */
	void exitFor_update(ChubbyParser.For_updateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_statement(ChubbyParser.While_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_statement(ChubbyParser.While_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ChubbyParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ChubbyParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpression(ChubbyParser.LogicalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpression(ChubbyParser.LogicalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpression(ChubbyParser.LogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpression(ChubbyParser.LogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(ChubbyParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(ChubbyParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(ChubbyParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(ChubbyParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(ChubbyParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(ChubbyParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(ChubbyParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(ChubbyParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(ChubbyParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(ChubbyParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(ChubbyParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(ChubbyParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(ChubbyParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(ChubbyParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(ChubbyParser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(ChubbyParser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#object_creation}.
	 * @param ctx the parse tree
	 */
	void enterObject_creation(ChubbyParser.Object_creationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#object_creation}.
	 * @param ctx the parse tree
	 */
	void exitObject_creation(ChubbyParser.Object_creationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChubbyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(ChubbyParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChubbyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(ChubbyParser.LiteralContext ctx);
}