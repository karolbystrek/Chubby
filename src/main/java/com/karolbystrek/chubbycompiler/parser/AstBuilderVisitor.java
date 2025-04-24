package com.karolbystrek.chubbycompiler.parser;

import com.karolbystrek.chubbycompiler.*;
import com.karolbystrek.chubbycompiler.ast.*;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;
import java.util.List;

public class AstBuilderVisitor extends ChubbyBaseVisitor<AstNode> {

    @Override
    public AstNode visitClass_definition(ChubbyParser.Class_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());

        String className = ctx.IDENTIFIER().getText();

        List<AstNode> members = new ArrayList<>();
        if (ctx != null && ctx.class_member() != null) {
            for (ChubbyParser.Class_memberContext memberCtx : ctx.class_member()) {
                members.add(visit(memberCtx));
            }
        }

        return new ClassDefinitionNode(
                visibility,
                className,
                members,
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine()
        );
    }

    @Override
    public AstNode visitFunction_definition(ChubbyParser.Function_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());
        boolean isStatic = ctx.STATIC() != null;
        String functionName = ctx.IDENTIFIER().getText();

        List<ParameterNode> parameters = new ArrayList<>();
        if (ctx.parameter_list() != null) {
            for (ChubbyParser.ParameterContext paramCtx : ctx.parameter_list().parameter()) {
                parameters.add((ParameterNode) visit(paramCtx));
            }
        }

        TypeNode returnType = (TypeNode) visit(ctx.return_type());

        List<AstNode> body = new ArrayList<>();
        if (ctx != null && ctx.statement() != null) {
            for (ChubbyParser.StatementContext stmtCtx : ctx.statement()) {
                AstNode visitedStmt = visit(stmtCtx);
                if (visitedStmt != null) {
                    body.add(visitedStmt);
                }
            }

        }

        return new FunctionDefinitionNode(visibility, isStatic, functionName, parameters, returnType, body,
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitConstructor_definition(ChubbyParser.Constructor_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());

        String constructorName = ctx.IDENTIFIER().getText();

        List<ParameterNode> parameters = new ArrayList<>();
        if (ctx.parameter_list() != null) {
            for (ChubbyParser.ParameterContext paramCtx : ctx.parameter_list().parameter()) {
                parameters.add((ParameterNode) visit(paramCtx));
            }
        }

        List<AstNode> body = new ArrayList<>();
        if (ctx != null && ctx.statement() != null) {
            for (ChubbyParser.StatementContext stmtCtx : ctx.statement()) {
                AstNode visitedStmt = visit(stmtCtx);
                if (visitedStmt != null) {
                    body.add(visitedStmt);
                }
            }
        }

        return new ConstructorDefinitionNode(visibility, constructorName, parameters, body,
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitVariable_definition(ChubbyParser.Variable_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());
        boolean isStatic = ctx.STATIC() != null;
        boolean isConst = ctx.CONST() != null;

        TypeNode baseType = (TypeNode) visit(ctx.type_specifier());
        int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
        TypeNode type = new TypeNode(baseType.getBaseTypeName(), arrayDimensions, baseType.getLineNumber(), baseType.getColumnNumber());

        String name = ctx.IDENTIFIER().getText();
        AstNode initializer = null;
        if (ctx.expression() != null) {
            initializer = visit(ctx.expression());
        }
        return new VariableDefinitionNode(visibility, type, name, isStatic, isConst, initializer,
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitParameter(ChubbyParser.ParameterContext ctx) {
        TypeNode baseType = (TypeNode) visit(ctx.type_specifier());
        int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
        TypeNode type = new TypeNode(baseType.getBaseTypeName(), arrayDimensions, baseType.getLineNumber(), baseType.getColumnNumber());
        String name = ctx.IDENTIFIER().getText();
        return new ParameterNode(type, name, ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitReturn_type(ChubbyParser.Return_typeContext ctx) {
        if (ctx.VOID() != null) {
            return new TypeNode(ctx.VOID().getText(), 0, ctx.start.getLine(), ctx.start.getCharPositionInLine());
        } else if (ctx.type_specifier() != null) {
            TypeNode baseTypeNode = (TypeNode) visit(ctx.type_specifier());
            int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
            return new TypeNode(baseTypeNode.getBaseTypeName(), arrayDimensions, baseTypeNode.getLineNumber(), baseTypeNode.getColumnNumber());
        }
        throw new IllegalStateException("Invalid return_type context: " + ctx.getText());
    }

    @Override
    public AstNode visitType_specifier(ChubbyParser.Type_specifierContext ctx) {
        String typeName;
        if (ctx.BYTE() != null) typeName = ctx.BYTE().getText();
        else if (ctx.BOOL() != null) typeName = ctx.BOOL().getText();
        else if (ctx.INT() != null) typeName = ctx.INT().getText();
        else if (ctx.FLOAT() != null) typeName = ctx.FLOAT().getText();
        else if (ctx.DOUBLE() != null) typeName = ctx.DOUBLE().getText();
        else if (ctx.CHAR() != null) typeName = ctx.CHAR().getText();
        else if (ctx.STRING() != null) typeName = ctx.STRING().getText();
        else if (ctx.LONG() != null) typeName = ctx.LONG().getText();
        else if (ctx.qualified_identifier() != null) typeName = ctx.qualified_identifier().getText();
        else {
            throw new IllegalStateException("Invalid type_specifier context: " + ctx.getText());
        }
        return new TypeNode(typeName, ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitStatement(ChubbyParser.StatementContext ctx) {
        if (ctx.block_statement() != null) {
            return visit(ctx.block_statement());
        } else if (ctx.simple_statement() != null) {
            return visit(ctx.simple_statement());
        }
        return null;
    }

    @Override
    public AstNode visitBlock_statement(ChubbyParser.Block_statementContext ctx) {
        if (ctx.if_statement() != null) return visit(ctx.if_statement());
        if (ctx.for_statement() != null) return visit(ctx.for_statement());
        if (ctx.while_statement() != null) return visit(ctx.while_statement());
        if (ctx.try_catch_statement() != null) return visit(ctx.try_catch_statement());
        return null;
    }

    @Override
    public AstNode visitSimple_statement(ChubbyParser.Simple_statementContext ctx) {
        if (ctx.local_variable_declaration() != null) return visit(ctx.local_variable_declaration());
        if (ctx.assignment_statement() != null) return visit(ctx.assignment_statement());
        if (ctx.return_statement() != null) return visit(ctx.return_statement());
        if (ctx.break_statement() != null) return visit(ctx.break_statement());
        if (ctx.continue_statement() != null) return visit(ctx.continue_statement());
        if (ctx.throw_statement() != null) return visit(ctx.throw_statement());
        if (ctx.expression() != null) {
            // TODO: Handle expression used as a statement (e.g., function call)
            // You might need a specific ExpressionStatementNode or just return the expression node
            AstNode exprNode = visit(ctx.expression());
            // Example: return new ExpressionStatementNode(exprNode, ...);
            return exprNode; // Or wrap it
        }
        return null; // Should not happen
    }

    @Override
    public AstNode visitChildren(RuleNode node) {
        System.err.println("Warning: Visiting children of unhandled node: " + node.getClass().getSimpleName());
        return super.visitChildren(node);
    }

    private Visibility getVisibility(ChubbyParser.Visibility_modifierContext ctx) {
        if (ctx == null) {
            return Visibility.PUBLIC;
        }
        if (ctx.PUBLIC() != null) {
            return Visibility.PUBLIC;
        } else if (ctx.PROTECTED() != null) {
            return Visibility.PROTECTED;
        } else if (ctx.PRIVATE() != null) {
            return Visibility.PRIVATE;
        }
        return Visibility.PRIVATE;
    }
}
