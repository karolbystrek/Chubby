package com.karolbystrek.chubbycompiler.parser;

import com.karolbystrek.chubbycompiler.*;
import com.karolbystrek.chubbycompiler.ast.*;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;
import java.util.List;

public class AstBuilderVisitor extends ChubbyBaseVisitor<AstNode> {

    @Override
    public AstNode visitClass_definition(ChubbyParser.Class_definitionContext ctx) {
        VisibilityModifierNode visibilityNode = (VisibilityModifierNode) visit(ctx.visibility_modifier());
        Visibility visibility = visibilityNode != null ?
                visibilityNode.getVisibility() : Visibility.PRIVATE;

        String className = ctx.IDENTIFIER().getText();

        String superclassName = null;
        if (ctx.extends_clause() != null) {
            superclassName = ctx.extends_clause().qualified_identifier().getText();
        }

        List<String> implementedInterfaceNames = new ArrayList<>();
        if (ctx.implements_clause() != null) {
            ctx.implements_clause().qualified_identifier().forEach(
                    qualifiedIdentifier -> implementedInterfaceNames.add(qualifiedIdentifier.getText())
            );
        }

        List<AstNode> members = new ArrayList<>();
        if (ctx.class_body() != null) {
            ctx.class_body().class_member().forEach(
                    member -> members.add(visit(member))
            );
        }

        return new ClassDefinitionNode(
                visibility,
                className,
                superclassName,
                implementedInterfaceNames,
                members,
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine()
        );
    }

    @Override
    public AstNode visitFunction_definition(ChubbyParser.Function_definitionContext ctx) {
        VisibilityModifierNode visibilityNode = (VisibilityModifierNode) visit(ctx.visibility_modifier());
        Visibility visibility = visibilityNode != null ?
                visibilityNode.getVisibility() : Visibility.PRIVATE;
        boolean isStatic = ctx.STATIC() != null;
        String functionName = ctx.IDENTIFIER().getText();

        List<AstNode> parameters = new ArrayList<>();
        if (ctx.parameter_list() != null) {
            ctx.parameter_list().parameter().forEach(
                    parameter -> parameters.add(visit(parameter))
            );
        }

        AstNode returnType = visit(ctx.return_type());

        List<AstNode> body = new ArrayList<>();
        if (ctx.function_body() != null) {
            ctx.function_body().statement().forEach(
                    statement -> body.add(visit(statement))
            );
        }

        return new FunctionDefinitionNode(visibility, isStatic, functionName, parameters, returnType, body,
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitConstructor_definition(ChubbyParser.Constructor_definitionContext ctx) {
        String constructorName = ctx.IDENTIFIER().getText();

        List<AstNode> parameters = new ArrayList<>();
        if (ctx.parameter_list() != null) {
            ctx.parameter_list().parameter().forEach(
                    parameter -> parameters.add(visit(parameter))
            );
        }

        List<AstNode> body = new ArrayList<>();
        if (ctx.constructor_body() != null) {
            ctx.constructor_body().statement().forEach(
                    statement -> body.add(visit(statement))
            );
        }

        return new ConstructorDefinitionNode(constructorName, parameters, body,
                ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitVisibility_modifier(ChubbyParser.Visibility_modifierContext ctx) {
        Visibility visibility;
        if (ctx.PUBLIC() != null) {
            visibility = Visibility.PUBLIC;
        } else if (ctx.PROTECTED() != null) {
            visibility = Visibility.PROTECTED;
        } else if (ctx.PRIVATE() != null) {
            visibility = Visibility.PRIVATE;
        } else {
            visibility = Visibility.PRIVATE;
        }
        return new VisibilityModifierNode(visibility, ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    @Override
    public AstNode visitChildren(RuleNode node) {
        return super.visitChildren(node);
    }
}
