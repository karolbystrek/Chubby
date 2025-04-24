// Generated from C:/Users/chame/Desktop/sem4/kompilatory/OwnProgrammingLanguage/src/main/antlr4/com/karolbystrek/chubbycompiler/Chubby.g4 by ANTLR 4.13.2
package com.karolbystrek.chubbycompiler;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ChubbyParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ChubbyVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ChubbyParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#import_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_statement(ChubbyParser.Import_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#qualified_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_identifier(ChubbyParser.Qualified_identifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#class_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_definition(ChubbyParser.Class_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#class_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_body(ChubbyParser.Class_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#class_member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_member(ChubbyParser.Class_memberContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#constructor_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_definition(ChubbyParser.Constructor_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#constructor_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_body(ChubbyParser.Constructor_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#variable_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_definition(ChubbyParser.Variable_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#function_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_definition(ChubbyParser.Function_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#visibility_modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility_modifier(ChubbyParser.Visibility_modifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#parameter_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_list(ChubbyParser.Parameter_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(ChubbyParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#type_specifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_specifier(ChubbyParser.Type_specifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#return_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_type(ChubbyParser.Return_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#function_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_body(ChubbyParser.Function_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ChubbyParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#block_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_statement(ChubbyParser.Block_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#simple_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_statement(ChubbyParser.Simple_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#local_variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_variable_declaration(ChubbyParser.Local_variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(ChubbyParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLvalue(ChubbyParser.LvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#assignment_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_operator(ChubbyParser.Assignment_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#return_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(ChubbyParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#break_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_statement(ChubbyParser.Break_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#continue_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_statement(ChubbyParser.Continue_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#throw_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrow_statement(ChubbyParser.Throw_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#try_catch_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTry_catch_statement(ChubbyParser.Try_catch_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(ChubbyParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#for_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_statement(ChubbyParser.For_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#for_init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_init(ChubbyParser.For_initContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#for_update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_update(ChubbyParser.For_updateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#while_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_statement(ChubbyParser.While_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ChubbyParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(ChubbyParser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(ChubbyParser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(ChubbyParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(ChubbyParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(ChubbyParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(ChubbyParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(ChubbyParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpression(ChubbyParser.PostfixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(ChubbyParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#argument_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument_list(ChubbyParser.Argument_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#object_creation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_creation(ChubbyParser.Object_creationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChubbyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ChubbyParser.LiteralContext ctx);
}