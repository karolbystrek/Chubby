package com.karolbystrek.chubbycompiler.parser;

import com.karolbystrek.chubbycompiler.*;
import com.karolbystrek.chubbycompiler.ast.*;
import com.karolbystrek.chubbycompiler.ast.expression.*;
import com.karolbystrek.chubbycompiler.ast.expression.literal.*; // Import specific literal nodes if they exist, otherwise use generic
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.block.*;
import com.karolbystrek.chubbycompiler.ast.statement.simple.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AstBuilderVisitor extends ChubbyBaseVisitor<AstNode> {

    // Helper to get line number from context or token
    private int line(ParserRuleContext ctx) { return ctx.start.getLine(); }
    private int col(ParserRuleContext ctx) { return ctx.start.getCharPositionInLine(); }
    private int line(Token token) { return token.getLine(); }
    private int col(Token token) { return token.getCharPositionInLine(); }

    // Helper to extract statements within specific boundaries (e.g., between THEN and ELSE)
    private List<StatementNode> extractStatements(ParserRuleContext parentCtx, int startChildIndex, int endChildIndex) {
        List<StatementNode> statements = new ArrayList<>();
        for (int i = startChildIndex; i < endChildIndex; i++) {
            ParseTree child = parentCtx.getChild(i);
            if (child instanceof ChubbyParser.StatementContext) {
                AstNode visitedStmt = visit(child);
                if (visitedStmt instanceof StatementNode) {
                    statements.add((StatementNode) visitedStmt);
                } else if (visitedStmt != null) {
                    logWarning("Expected StatementNode but got " + visitedStmt.getClass().getSimpleName() + " within block", (ParserRuleContext) child);
                }
            }
            // Stop if we encounter unexpected block delimiters within the range (might indicate grammar ambiguity or visitor error)
            else if (child instanceof TerminalNode) {
                 int type = ((TerminalNode) child).getSymbol().getType();
                 if (isBlockDelimiter(type)) {
                     logWarning("Unexpected block delimiter " + ((TerminalNode) child).getText() + " found while extracting statements.", parentCtx);
                     break; // Stop extracting further to avoid incorrect association
                 }
            }
        }
        return statements;
    }

    // Helper to identify tokens that delimit statement blocks
    private boolean isBlockDelimiter(int tokenType) {
        switch (tokenType) {
            case ChubbyLexer.THEN:
            case ChubbyLexer.ELSIF:
            case ChubbyLexer.ELSE:
            case ChubbyLexer.ENDIF:
            case ChubbyLexer.DO:
            case ChubbyLexer.ENDFOR:
            case ChubbyLexer.ENDWHILE:
            case ChubbyLexer.CATCH:
            case ChubbyLexer.FINALLY:
            case ChubbyLexer.ENDTRY:
            case ChubbyLexer.ENDFUNCTION:
            case ChubbyLexer.ENDCONSTRUCTOR:
            case ChubbyLexer.ENDCLASS: // Might be relevant in some contexts
                return true;
            default:
                return false;
        }
    }


    // Helper for logging warnings
    private void logWarning(String message, ParserRuleContext ctx) {
        System.err.println("Warning: " + message + " at " + line(ctx) + ":" + col(ctx) + " (" + ctx.getText() + ")");
    }
     private void logError(String message, ParserRuleContext ctx) {
        System.err.println("Error: " + message + " at " + line(ctx) + ":" + col(ctx) + " (" + ctx.getText() + ")");
        // Consider throwing a custom exception here for critical errors
        // throw new AstBuildException(message, ctx);
    }

    @Override
    public AstNode visitProgram(ChubbyParser.ProgramContext ctx) {
        List<ImportStatementNode> imports = new ArrayList<>();
        if (ctx.import_statement() != null) {
            for (ChubbyParser.Import_statementContext impCtx : ctx.import_statement()) {
                AstNode visited = visit(impCtx);
                if (visited instanceof ImportStatementNode) {
                    imports.add((ImportStatementNode) visited);
                } else {
                     logWarning("Expected ImportStatementNode", impCtx);
                }
            }
        }

        List<ClassDefinitionNode> classes = new ArrayList<>();
        if (ctx.class_definition() != null) {
            for (ChubbyParser.Class_definitionContext classCtx : ctx.class_definition()) {
                 AstNode visited = visit(classCtx);
                 if (visited instanceof ClassDefinitionNode) {
                    classes.add((ClassDefinitionNode) visited);
                 } else {
                     logWarning("Expected ClassDefinitionNode", classCtx);
                 }
            }
        }
        if (classes.isEmpty()) {
            logError("Program must contain at least one class definition", ctx);
            // Return a dummy node or null, depending on error handling strategy
            return new ProgramNode(imports, Collections.emptyList(), line(ctx), col(ctx));
        }

        return new ProgramNode(imports, classes, line(ctx), col(ctx));
    }

    @Override
    public AstNode visitImport_statement(ChubbyParser.Import_statementContext ctx) {
        String qualifiedId = ctx.qualified_identifier().getText();
        return new ImportStatementNode(qualifiedId, line(ctx), col(ctx));
    }

    @Override
    public AstNode visitClass_definition(ChubbyParser.Class_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());
        String className = ctx.IDENTIFIER().getText();

        List<ClassMemberNode> members = new ArrayList<>();
        int memberStartIndex = -1;
        int memberEndIndex = -1;

        // Find indices of children that define the members block
        for(int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.IDENTIFIER) {
                memberStartIndex = i + 1; // Members start after the class IDENTIFIER
            }
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.ENDCLASS) {
                memberEndIndex = i; // Members end before ENDCLASS
                break;
            }
        }

        if (memberStartIndex != -1 && memberEndIndex != -1) {
            for (int i = memberStartIndex; i < memberEndIndex; i++) {
                 ParseTree child = ctx.getChild(i);
                 if (child instanceof ChubbyParser.Class_memberContext) {
                     AstNode member = visit(child);
                     if (member instanceof ClassMemberNode) {
                         members.add((ClassMemberNode) member);
                     } else if (member != null) {
                         logWarning("Unexpected node type " + member.getClass().getSimpleName() + " found in class definition", (ParserRuleContext)child);
                     }
                 }
                 // Skip whitespace or other non-member nodes if necessary
            }
        } else {
             logError("Could not determine member boundaries for class " + className, ctx);
        }


        return new ClassDefinitionNode(visibility, className, members, line(ctx), col(ctx));
    }

     @Override
     public AstNode visitClass_member(ChubbyParser.Class_memberContext ctx) {
         // Delegate to the specific member type visitor
         if (ctx.function_definition() != null) return visit(ctx.function_definition());
         if (ctx.constructor_definition() != null) return visit(ctx.constructor_definition());
         if (ctx.variable_definition() != null) return visit(ctx.variable_definition());
         logError("Unknown class member type", ctx);
         return null;
     }


    @Override
    public AstNode visitFunction_definition(ChubbyParser.Function_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());
        boolean isStatic = ctx.STATIC() != null;
        String functionName = ctx.IDENTIFIER().getText();

        List<ParameterNode> parameters = new ArrayList<>();
        if (ctx.parameter_list() != null) {
            for (ChubbyParser.ParameterContext paramCtx : ctx.parameter_list().parameter()) {
                 AstNode visited = visit(paramCtx);
                 if (visited instanceof ParameterNode) {
                    parameters.add((ParameterNode) visited);
                 } else {
                     logWarning("Expected ParameterNode", paramCtx);
                 }
            }
        }

        TypeNode returnType = (TypeNode) visit(ctx.return_type());

        // Extract statements between return_type and ENDFUNCTION
        List<StatementNode> body = new ArrayList<>();
        int bodyStartIndex = -1;
        int bodyEndIndex = -1;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) == ctx.return_type()) {
                bodyStartIndex = i + 1;
            }
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.ENDFUNCTION) {
                bodyEndIndex = i;
                break;
            }
        }

        if (bodyStartIndex != -1 && bodyEndIndex != -1) {
            body = extractStatements(ctx, bodyStartIndex, bodyEndIndex);
        } else {
            logError("Could not determine body boundaries for function " + functionName, ctx);
        }

        return new FunctionDefinitionNode(visibility, isStatic, functionName, parameters, returnType, body,
                line(ctx), col(ctx));
    }

    @Override
    public AstNode visitConstructor_definition(ChubbyParser.Constructor_definitionContext ctx) {
        // Constructor visibility is optional in grammar, default to public if missing
        Visibility visibility = (ctx.visibility_modifier() != null) ? getVisibility(ctx.visibility_modifier()) : Visibility.PUBLIC;
        String constructorName = ctx.IDENTIFIER().getText();

        List<ParameterNode> parameters = new ArrayList<>();
        if (ctx.parameter_list() != null) {
            for (ChubbyParser.ParameterContext paramCtx : ctx.parameter_list().parameter()) {
                 AstNode visited = visit(paramCtx);
                 if (visited instanceof ParameterNode) {
                    parameters.add((ParameterNode) visited);
                 } else {
                     logWarning("Expected ParameterNode", paramCtx);
                 }
            }
        }

        // Extract statements between RIGHT_PAREN and ENDCONSTRUCTOR
        List<StatementNode> body = new ArrayList<>();
        int bodyStartIndex = -1;
        int bodyEndIndex = -1;
        for (int i = 0; i < ctx.getChildCount(); i++) {
             if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.RIGHT_PAREN) {
                bodyStartIndex = i + 1;
            }
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.ENDCONSTRUCTOR) {
                bodyEndIndex = i;
                break;
            }
        }

        if (bodyStartIndex != -1 && bodyEndIndex != -1) {
            body = extractStatements(ctx, bodyStartIndex, bodyEndIndex);
        } else {
            logError("Could not determine body boundaries for constructor " + constructorName, ctx);
        }

        return new ConstructorDefinitionNode(visibility, constructorName, parameters, body,
                line(ctx), col(ctx));
    }

    @Override
    public AstNode visitVariable_definition(ChubbyParser.Variable_definitionContext ctx) {
        Visibility visibility = getVisibility(ctx.visibility_modifier());
        boolean isStatic = ctx.STATIC() != null;
        boolean isConst = ctx.CONST() != null;

        TypeNode baseType = (TypeNode) visit(ctx.type_specifier());
        // Count array dimensions specified *after* the type specifier
        int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
        TypeNode type = new TypeNode(baseType.getBaseTypeName(), arrayDimensions, baseType.getLineNumber(), baseType.getColumnNumber());

        String name = ctx.IDENTIFIER().getText();
        ExpressionNode initializer = null;
        if (ctx.expression() != null) {
             AstNode initNode = visit(ctx.expression());
             if (initNode instanceof ExpressionNode) {
                 initializer = (ExpressionNode) initNode;
             } else {
                 logError("Expected ExpressionNode for variable initializer", ctx.expression());
             }
        }

        return new VariableDefinitionNode(visibility, type, name, isStatic, isConst, initializer,
                line(ctx), col(ctx));
    }

    @Override
    public AstNode visitParameter(ChubbyParser.ParameterContext ctx) {
        TypeNode baseType = (TypeNode) visit(ctx.type_specifier());
        // Count array dimensions specified *after* the type specifier
        int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
        TypeNode type = new TypeNode(baseType.getBaseTypeName(), arrayDimensions, baseType.getLineNumber(), baseType.getColumnNumber());
        String name = ctx.IDENTIFIER().getText();
        return new ParameterNode(type, name, line(ctx), col(ctx));
    }

    @Override
    public AstNode visitReturn_type(ChubbyParser.Return_typeContext ctx) {
        if (ctx.VOID() != null) {
            return new TypeNode("void", 0, line(ctx.VOID().getSymbol()), col(ctx.VOID().getSymbol()));
        } else if (ctx.type_specifier() != null) {
            TypeNode baseTypeNode = (TypeNode) visit(ctx.type_specifier());
            // Count array dimensions specified *after* the type specifier
            int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
            return new TypeNode(baseTypeNode.getBaseTypeName(), arrayDimensions, baseTypeNode.getLineNumber(), baseTypeNode.getColumnNumber());
        }
        logError("Invalid return type", ctx);
        return new TypeNode("void", 0, line(ctx), col(ctx)); // Fallback
    }

    @Override
    public AstNode visitType_specifier(ChubbyParser.Type_specifierContext ctx) {
        String typeName;
        Token typeToken;

        if (ctx.BYTE() != null) { typeName = ctx.BYTE().getText(); typeToken = ctx.BYTE().getSymbol(); }
        else if (ctx.BOOL() != null) { typeName = ctx.BOOL().getText(); typeToken = ctx.BOOL().getSymbol(); }
        else if (ctx.INT() != null) { typeName = ctx.INT().getText(); typeToken = ctx.INT().getSymbol(); }
        else if (ctx.FLOAT() != null) { typeName = ctx.FLOAT().getText(); typeToken = ctx.FLOAT().getSymbol(); }
        else if (ctx.DOUBLE() != null) { typeName = ctx.DOUBLE().getText(); typeToken = ctx.DOUBLE().getSymbol(); }
        else if (ctx.CHAR() != null) { typeName = ctx.CHAR().getText(); typeToken = ctx.CHAR().getSymbol(); }
        else if (ctx.STRING() != null) { typeName = ctx.STRING().getText(); typeToken = ctx.STRING().getSymbol(); }
        else if (ctx.LONG() != null) { typeName = ctx.LONG().getText(); typeToken = ctx.LONG().getSymbol(); }
        else if (ctx.qualified_identifier() != null) {
            typeName = ctx.qualified_identifier().getText();
            typeToken = ctx.qualified_identifier().start; // Use start token of identifier
        } else {
            logError("Unknown type specifier", ctx);
            return new TypeNode("<<error>>", 0, line(ctx), col(ctx)); // Error type
        }
        // Array dimensions are handled in parameter, return_type, variable_definition, etc.
        return new TypeNode(typeName, 0, line(typeToken), col(typeToken));
    }

    @Override
    public AstNode visitStatement(ChubbyParser.StatementContext ctx) {
        if (ctx.block_statement() != null) {
            return visit(ctx.block_statement());
        } else if (ctx.simple_statement() != null) {
            // Simple statements require a semicolon according to grammar
            return visit(ctx.simple_statement());
        }
        logWarning("Empty or unhandled statement context", ctx);
        return null;
    }

    @Override
    public AstNode visitBlock_statement(ChubbyParser.Block_statementContext ctx) {
        if (ctx.if_statement() != null) return visit(ctx.if_statement());
        if (ctx.for_statement() != null) return visit(ctx.for_statement());
        if (ctx.while_statement() != null) return visit(ctx.while_statement());
        if (ctx.try_catch_statement() != null) return visit(ctx.try_catch_statement());
        logError("Unhandled block statement type", ctx);
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
            AstNode exprNode = visit(ctx.expression());
            if (exprNode instanceof ExpressionNode) {
                return new ExpressionStatementNode((ExpressionNode) exprNode, line(ctx), col(ctx));
            } else {
                // Log error only if exprNode is not null but wrong type
                if (exprNode != null) {
                    logError("Expected ExpressionNode but got " + exprNode.getClass().getSimpleName() + " for expression statement", ctx.expression());
                } else {
                     logError("Failed to parse expression for expression statement", ctx.expression());
                }
                return null;
            }
        }
        logError("Unhandled simple statement type", ctx);
        return null;
    }

     @Override
    public AstNode visitLocal_variable_declaration(ChubbyParser.Local_variable_declarationContext ctx) {
        TypeNode baseType = (TypeNode) visit(ctx.type_specifier());
        // Count array dimensions specified *after* the type specifier
        int arrayDimensions = (ctx.LEFT_SQUARE() != null) ? ctx.LEFT_SQUARE().size() : 0;
        TypeNode type = new TypeNode(baseType.getBaseTypeName(), arrayDimensions, baseType.getLineNumber(), baseType.getColumnNumber());

        String name = ctx.IDENTIFIER().getText();
        ExpressionNode initializer = null;
        if (ctx.expression() != null) {
            AstNode initNode = visit(ctx.expression());
            if (initNode instanceof ExpressionNode) {
                initializer = (ExpressionNode) initNode;
            } else {
                logError("Expected ExpressionNode for local variable initializer", ctx.expression());
            }
        }
        return new LocalVariableDeclarationNode(type, name, initializer, line(ctx), col(ctx));
    }

    @Override
    public AstNode visitAssignment_statement(ChubbyParser.Assignment_statementContext ctx) {
        AstNode lValueNode = visit(ctx.lvalue());
        AssignmentStatementNode.AssignmentOperator operator = getAssignmentOperator(ctx.assignment_operator());
        AstNode rValueNode = visit(ctx.expression());

        if (!(lValueNode instanceof LValueNode)) {
            logError("Expected LValueNode but got " + (lValueNode != null ? lValueNode.getClass().getSimpleName() : "null") + " for assignment target", ctx.lvalue());
            return null;
        }
        if (!(rValueNode instanceof ExpressionNode)) {
             logError("Expected ExpressionNode for assignment value", ctx.expression());
            return null;
        }

        return new AssignmentStatementNode((LValueNode) lValueNode, operator, (ExpressionNode) rValueNode,
                line(ctx), col(ctx));
    }

    @Override
    public AstNode visitReturn_statement(ChubbyParser.Return_statementContext ctx) {
        ExpressionNode returnValue = null;
        if (ctx.expression() != null) {
            AstNode expr = visit(ctx.expression());
            if (expr instanceof ExpressionNode) {
                returnValue = (ExpressionNode) expr;
            } else {
                logError("Expected ExpressionNode for return value", ctx.expression());
                // Return statement without value might still be valid depending on context (void function)
                // Semantic analysis should check this. For AST, we just note the parsing failure.
            }
        }
        return new ReturnStatementNode(returnValue, line(ctx), col(ctx));
    }

    @Override
    public AstNode visitBreak_statement(ChubbyParser.Break_statementContext ctx) {
        return new BreakStatementNode(line(ctx), col(ctx));
    }

    @Override
    public AstNode visitContinue_statement(ChubbyParser.Continue_statementContext ctx) {
        return new ContinueStatementNode(line(ctx), col(ctx));
    }

    @Override
    public AstNode visitThrow_statement(ChubbyParser.Throw_statementContext ctx) {
        AstNode expr = visit(ctx.expression());
        if (expr instanceof ExpressionNode) {
            return new ThrowStatementNode((ExpressionNode) expr, line(ctx), col(ctx));
        } else {
            logError("Expected ExpressionNode for throw statement", ctx.expression());
            return null; // Throw requires an expression
        }
    }

    @Override
    public AstNode visitTry_catch_statement(ChubbyParser.Try_catch_statementContext ctx) {
        int currentChildIndex = 0;
        BlockNode tryBody = null;
        List<CatchClauseNode> catchClauses = new ArrayList<>();
        FinallyClauseNode finallyClause = null;

        // Find TRY token
        while (currentChildIndex < ctx.getChildCount() && !(ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.TRY)) {
            currentChildIndex++;
        }
        if (currentChildIndex >= ctx.getChildCount()) { logError("Missing TRY token", ctx); return null; }
        int tryBodyStartIndex = currentChildIndex + 1;

        // Find first CATCH or FINALLY or ENDTRY to delimit try body
        int tryBodyEndIndex = tryBodyStartIndex;
        while (tryBodyEndIndex < ctx.getChildCount()) {
            ParseTree child = ctx.getChild(tryBodyEndIndex);
            if (child instanceof TerminalNode) {
                int type = ((TerminalNode) child).getSymbol().getType();
                if (type == ChubbyLexer.CATCH || type == ChubbyLexer.FINALLY || type == ChubbyLexer.ENDTRY) {
                    break;
                }
            }
            tryBodyEndIndex++;
        }
        if (tryBodyEndIndex >= ctx.getChildCount()) { logError("Missing ENDTRY or CATCH/FINALLY after TRY", ctx); return null; }

        List<StatementNode> tryStatements = extractStatements(ctx, tryBodyStartIndex, tryBodyEndIndex);
        // Use position of TRY token for the block node
        tryBody = new BlockNode(tryStatements, line(ctx.TRY().getSymbol()), col(ctx.TRY().getSymbol()));
        currentChildIndex = tryBodyEndIndex;

        // Process CATCH clauses
        while (currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.CATCH) {
            TerminalNode catchTokenNode = (TerminalNode) ctx.getChild(currentChildIndex);
            currentChildIndex++; // Move past CATCH
            // Expect LEFT_PAREN, parameter, RIGHT_PAREN
            if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.LEFT_PAREN)) { logError("Expected '(' after CATCH", ctx); break; }
            currentChildIndex++;
            if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof ChubbyParser.ParameterContext)) { logError("Expected parameter after CATCH '('", ctx); break; }
            ParameterNode param = (ParameterNode) visit(ctx.getChild(currentChildIndex));
            currentChildIndex++;
            if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.RIGHT_PAREN)) { logError("Expected ')' after CATCH parameter", ctx); break; }
            currentChildIndex++;

            // Extract catch body statements
            int catchBodyStartIndex = currentChildIndex;
            int catchBodyEndIndex = catchBodyStartIndex;
             while (catchBodyEndIndex < ctx.getChildCount()) {
                ParseTree child = ctx.getChild(catchBodyEndIndex);
                if (child instanceof TerminalNode) {
                    int type = ((TerminalNode) child).getSymbol().getType();
                    // Stop at next CATCH, FINALLY, or ENDTRY
                    if (type == ChubbyLexer.CATCH || type == ChubbyLexer.FINALLY || type == ChubbyLexer.ENDTRY) {
                        break;
                    }
                }
                catchBodyEndIndex++;
            }
             if (catchBodyEndIndex >= ctx.getChildCount()) { logError("Missing ENDTRY or next CATCH/FINALLY after CATCH block", ctx); break; }

            List<StatementNode> catchStatements = extractStatements(ctx, catchBodyStartIndex, catchBodyEndIndex);
            BlockNode catchBody = new BlockNode(catchStatements, line(param), col(param)); // Position near parameter
            catchClauses.add(new CatchClauseNode(param, catchBody, line(catchTokenNode.getSymbol()), col(catchTokenNode.getSymbol())));
            currentChildIndex = catchBodyEndIndex;
        }

        // Process FINALLY clause
        if (currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.FINALLY) {
             TerminalNode finallyTokenNode = (TerminalNode) ctx.getChild(currentChildIndex);
             currentChildIndex++; // Move past FINALLY
             int finallyBodyStartIndex = currentChildIndex;
             int finallyBodyEndIndex = finallyBodyStartIndex;
             while (finallyBodyEndIndex < ctx.getChildCount() && !(ctx.getChild(finallyBodyEndIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(finallyBodyEndIndex)).getSymbol().getType() == ChubbyLexer.ENDTRY)) {
                 finallyBodyEndIndex++;
             }
             if (finallyBodyEndIndex >= ctx.getChildCount()) { logError("Missing ENDTRY after FINALLY", ctx); return null; }

             List<StatementNode> finallyStatements = extractStatements(ctx, finallyBodyStartIndex, finallyBodyEndIndex);
             BlockNode finallyBody = new BlockNode(finallyStatements, line(finallyTokenNode.getSymbol()), col(finallyTokenNode.getSymbol()));
             finallyClause = new FinallyClauseNode(finallyBody, line(finallyTokenNode.getSymbol()), col(finallyTokenNode.getSymbol()));
             currentChildIndex = finallyBodyEndIndex;
        }

        // Check for ENDTRY
        if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.ENDTRY)) {
            logError("Missing ENDTRY at the end of try-catch statement", ctx);
            // Still try to return what was parsed if possible
        }

        if (catchClauses.isEmpty() && finallyClause == null) {
             logError("Try statement must have at least one catch or finally block.", ctx);
             // Return null or a dummy node
             return null;
        }

        return new TryCatchStatementNode(tryBody, catchClauses, finallyClause, line(ctx), col(ctx));
    }


    @Override
    public AstNode visitIf_statement(ChubbyParser.If_statementContext ctx) {
        int currentChildIndex = 0;
        ExpressionNode mainCondition = null;
        BlockNode thenBranch = null;
        List<IfStatementNode.ElsifBranch> elsifBranches = new ArrayList<>();
        BlockNode elseBranch = null;

        // Find IF token
        while (currentChildIndex < ctx.getChildCount() && !(ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.IF)) {
            currentChildIndex++;
        }
        if (currentChildIndex >= ctx.getChildCount()) { logError("Missing IF token", ctx); return null; }
        currentChildIndex++; // Move past IF

        // Expect boolean_expression THEN
        if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof ChubbyParser.Boolean_expressionContext)) { logError("Expected condition after IF", ctx); return null; }
        mainCondition = (ExpressionNode) visit(ctx.getChild(currentChildIndex));
        currentChildIndex++;
        if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.THEN)) { logError("Expected THEN after IF condition", ctx); return null; }
        TerminalNode thenTokenNode = (TerminalNode) ctx.getChild(currentChildIndex);
        currentChildIndex++;

        // Extract THEN branch statements
        int thenBodyStartIndex = currentChildIndex;
        int thenBodyEndIndex = thenBodyStartIndex;
        while (thenBodyEndIndex < ctx.getChildCount()) {
            ParseTree child = ctx.getChild(thenBodyEndIndex);
            if (child instanceof TerminalNode) {
                int type = ((TerminalNode) child).getSymbol().getType();
                if (type == ChubbyLexer.ELSIF || type == ChubbyLexer.ELSE || type == ChubbyLexer.ENDIF) {
                    break;
                }
            }
            thenBodyEndIndex++;
        }
         if (thenBodyEndIndex >= ctx.getChildCount()) { logError("Missing ENDIF or ELSIF/ELSE after THEN block", ctx); return null; }

        List<StatementNode> thenStatements = extractStatements(ctx, thenBodyStartIndex, thenBodyEndIndex);
        thenBranch = new BlockNode(thenStatements, line(thenTokenNode.getSymbol()), col(thenTokenNode.getSymbol()));
        currentChildIndex = thenBodyEndIndex;

        // Process ELSIF branches
        while (currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.ELSIF) {
            TerminalNode elsifTokenNode = (TerminalNode) ctx.getChild(currentChildIndex);
            currentChildIndex++; // Move past ELSIF
            // Expect boolean_expression THEN
            if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof ChubbyParser.Boolean_expressionContext)) { logError("Expected condition after ELSIF", ctx); break; }
            ExpressionNode elsifCondition = (ExpressionNode) visit(ctx.getChild(currentChildIndex));
            currentChildIndex++;
            if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.THEN)) { logError("Expected THEN after ELSIF condition", ctx); break; }
            TerminalNode elsifThenTokenNode = (TerminalNode) ctx.getChild(currentChildIndex);
            currentChildIndex++;

            // Extract ELSIF body statements
            int elsifBodyStartIndex = currentChildIndex;
            int elsifBodyEndIndex = elsifBodyStartIndex;
            while (elsifBodyEndIndex < ctx.getChildCount()) {
                ParseTree child = ctx.getChild(elsifBodyEndIndex);
                if (child instanceof TerminalNode) {
                    int type = ((TerminalNode) child).getSymbol().getType();
                    // Stop at next ELSIF, ELSE, or ENDIF
                    if (type == ChubbyLexer.ELSIF || type == ChubbyLexer.ELSE || type == ChubbyLexer.ENDIF) {
                        break;
                    }
                }
                elsifBodyEndIndex++;
            }
            if (elsifBodyEndIndex >= ctx.getChildCount()) { logError("Missing ENDIF or next ELSIF/ELSE after ELSIF block", ctx); break; }

            List<StatementNode> elsifStatements = extractStatements(ctx, elsifBodyStartIndex, elsifBodyEndIndex);
            BlockNode elsifBody = new BlockNode(elsifStatements, line(elsifThenTokenNode.getSymbol()), col(elsifThenTokenNode.getSymbol()));
            elsifBranches.add(new IfStatementNode.ElsifBranch(elsifCondition, elsifBody));
            currentChildIndex = elsifBodyEndIndex;
        }

        // Process ELSE branch
        if (currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.ELSE) {
            TerminalNode elseTokenNode = (TerminalNode) ctx.getChild(currentChildIndex);
            currentChildIndex++; // Move past ELSE
            int elseBodyStartIndex = currentChildIndex;
            int elseBodyEndIndex = elseBodyStartIndex;
            while (elseBodyEndIndex < ctx.getChildCount() && !(ctx.getChild(elseBodyEndIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(elseBodyEndIndex)).getSymbol().getType() == ChubbyLexer.ENDIF)) {
                elseBodyEndIndex++;
            }
            if (elseBodyEndIndex >= ctx.getChildCount()) { logError("Missing ENDIF after ELSE", ctx); return null; }

            List<StatementNode> elseStatements = extractStatements(ctx, elseBodyStartIndex, elseBodyEndIndex);
            elseBranch = new BlockNode(elseStatements, line(elseTokenNode.getSymbol()), col(elseTokenNode.getSymbol()));
            currentChildIndex = elseBodyEndIndex;
        }

         // Check for ENDIF
        if (!(currentChildIndex < ctx.getChildCount() && ctx.getChild(currentChildIndex) instanceof TerminalNode && ((TerminalNode)ctx.getChild(currentChildIndex)).getSymbol().getType() == ChubbyLexer.ENDIF)) {
            logError("Missing ENDIF at the end of if statement", ctx);
            // Still try to return what was parsed if possible
        }

        // Validate required parts
        if (mainCondition == null || thenBranch == null) {
             logError("IF statement is missing condition or THEN branch", ctx);
             return null;
        }

        return new IfStatementNode(mainCondition, thenBranch, elsifBranches, elseBranch, line(ctx), col(ctx));
    }


    @Override
    public AstNode visitFor_statement(ChubbyParser.For_statementContext ctx) {
        StatementNode initialization = null;
        if (ctx.for_init() != null) {
             AstNode initNode = visit(ctx.for_init());
             if (initNode instanceof StatementNode) {
                initialization = (StatementNode) initNode;
             } else {
                 logWarning("Expected StatementNode for for-loop initialization", ctx.for_init());
             }
        }

        ExpressionNode condition = null;
        if (ctx.boolean_expression() != null) {
             AstNode condNode = visit(ctx.boolean_expression());
             if (condNode instanceof ExpressionNode) {
                condition = (ExpressionNode) condNode;
             } else {
                 logError("Expected ExpressionNode for for-loop condition", ctx.boolean_expression());
             }
        }

        StatementNode update = null;
        if (ctx.for_update() != null) {
             AstNode updateNode = visit(ctx.for_update());
             if (updateNode instanceof StatementNode) {
                update = (StatementNode) updateNode;
             } else {
                 logWarning("Expected StatementNode for for-loop update", ctx.for_update());
             }
        }

        // Extract body statements between DO and ENDFOR
        List<StatementNode> bodyStatements = new ArrayList<>();
        int bodyStartIndex = -1;
        int bodyEndIndex = -1;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.DO) {
                bodyStartIndex = i + 1;
            }
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.ENDFOR) {
                bodyEndIndex = i;
                break;
            }
        }

        BlockNode body = null;
        if (bodyStartIndex != -1 && bodyEndIndex != -1) {
             bodyStatements = extractStatements(ctx, bodyStartIndex, bodyEndIndex);
             // Use position of DO token for the block node
             body = new BlockNode(bodyStatements, line(ctx.DO().getSymbol()), col(ctx.DO().getSymbol()));
        } else {
            logError("Could not determine body boundaries for FOR loop", ctx);
            // Create an empty block to allow AST construction to proceed
            body = new BlockNode(Collections.emptyList(), line(ctx), col(ctx));
        }


        return new ForStatementNode(initialization, condition, update, body, line(ctx), col(ctx));
    }

    @Override
    public AstNode visitFor_init(ChubbyParser.For_initContext ctx) {
        // Can be local var decl or assignment
        if (ctx.local_variable_declaration() != null) {
            return visit(ctx.local_variable_declaration());
        } else if (ctx.assignment_statement() != null) {
            return visit(ctx.assignment_statement());
        }
        logError("Invalid for_init structure", ctx);
        return null;
    }

    @Override
    public AstNode visitFor_update(ChubbyParser.For_updateContext ctx) {
         // Can be assignment or just an expression (like function call, increment)
         if (ctx.assignment_statement() != null) {
            return visit(ctx.assignment_statement());
        } else if (ctx.expression() != null) {
            // Wrap expression in an ExpressionStatementNode
            AstNode exprNode = visit(ctx.expression());
            if (exprNode instanceof ExpressionNode) {
                 return new ExpressionStatementNode((ExpressionNode) exprNode, line(ctx), col(ctx));
            } else {
                 logError("Expected ExpressionNode for for_update expression", ctx.expression());
                 return null;
            }
        }
        logError("Invalid for_update structure", ctx);
        return null;
    }

    @Override
    public AstNode visitWhile_statement(ChubbyParser.While_statementContext ctx) {
        ExpressionNode condition = null;
        AstNode condNode = visit(ctx.boolean_expression());
        if (condNode instanceof ExpressionNode) {
            condition = (ExpressionNode) condNode;
        } else {
            logError("Expected ExpressionNode for while condition", ctx.boolean_expression());
            return null; // While loop requires a condition
        }

        // Extract body statements between DO and ENDWHILE
        List<StatementNode> bodyStatements = new ArrayList<>();
        int bodyStartIndex = -1;
        int bodyEndIndex = -1;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.DO) {
                bodyStartIndex = i + 1;
            }
            if (ctx.getChild(i) instanceof TerminalNode && ((TerminalNode)ctx.getChild(i)).getSymbol().getType() == ChubbyLexer.ENDWHILE) {
                bodyEndIndex = i;
                break;
            }
        }

        BlockNode body = null;
        if (bodyStartIndex != -1 && bodyEndIndex != -1) {
             bodyStatements = extractStatements(ctx, bodyStartIndex, bodyEndIndex);
             // Use position of DO token for the block node
             body = new BlockNode(bodyStatements, line(ctx.DO().getSymbol()), col(ctx.DO().getSymbol()));
        } else {
            logError("Could not determine body boundaries for WHILE loop", ctx);
            // Create an empty block
            body = new BlockNode(Collections.emptyList(), line(ctx), col(ctx));
        }

        return new WhileStatementNode(condition, body, line(ctx), col(ctx));
    }

    // --- Expression Visiting ---

    // Helper for binary expressions (handles left-associativity)
    private AstNode visitBinaryExpression(ParserRuleContext ctx, Class<? extends ParserRuleContext> operandCtxType, int... operatorTokenTypes) {
        List<? extends ParserRuleContext> operands = ctx.getRuleContexts(operandCtxType);
        if (operands == null || operands.isEmpty()) {
            logError("Missing operands for binary expression", ctx);
            return null;
        }

        AstNode left = visit(operands.get(0));
        if (!(left instanceof ExpressionNode)) {
             logError("Expected ExpressionNode for left operand", operands.get(0));
             return null;
        }
        ExpressionNode result = (ExpressionNode) left;

        // Track operator tokens found at this level
        List<Token> opTokens = new ArrayList<>();
        for (int type : operatorTokenTypes) {
            List<TerminalNode> nodes = ctx.getTokens(type);
            if (nodes != null) {
                nodes.forEach(n -> opTokens.add(n.getSymbol()));
            }
        }
        // Sort operators by their position in the text
        opTokens.sort(java.util.Comparator.comparingInt(Token::getTokenIndex));


        int currentOpIndex = 0;
        for (int i = 1; i < operands.size(); i++) {
            // Ensure we have a corresponding operator token
            if (currentOpIndex >= opTokens.size()) {
                logError("Mismatched number of operands and operators", ctx);
                break; // Stop processing this expression level
            }
            Token opToken = opTokens.get(currentOpIndex++);
            BinaryExpressionNode.Operator op = mapBinaryOperator(opToken.getType());
            if (op == null) {
                 logError("Unknown binary operator token type: " + opToken.getType(), ctx);
                 continue; // Skip this operation
            }

            AstNode right = visit(operands.get(i));
             if (!(right instanceof ExpressionNode)) {
                 logError("Expected ExpressionNode for right operand", operands.get(i));
                 // Decide how to handle: skip operation, return current result, or return null
                 continue; // Skip this operation
             }
             result = new BinaryExpressionNode(result, op, (ExpressionNode) right, line(opToken), col(opToken));
        }
        return result;
    }

    // Map ANTLR token type to BinaryExpressionNode.Operator
    private BinaryExpressionNode.Operator mapBinaryOperator(int tokenType) {
        switch (tokenType) {
            case ChubbyLexer.OR: return BinaryExpressionNode.Operator.OR;
            case ChubbyLexer.AND: return BinaryExpressionNode.Operator.AND;
            case ChubbyLexer.EQUAL: return BinaryExpressionNode.Operator.EQUAL;
            case ChubbyLexer.NOT_EQUAL: return BinaryExpressionNode.Operator.NOT_EQUAL;
            case ChubbyLexer.LESS: return BinaryExpressionNode.Operator.LESS;
            case ChubbyLexer.GREATER: return BinaryExpressionNode.Operator.GREATER;
            case ChubbyLexer.LESS_EQUAL: return BinaryExpressionNode.Operator.LESS_EQUAL;
            case ChubbyLexer.GREATER_EQUAL: return BinaryExpressionNode.Operator.GREATER_EQUAL;
            case ChubbyLexer.PLUS: return BinaryExpressionNode.Operator.PLUS;
            case ChubbyLexer.MINUS: return BinaryExpressionNode.Operator.MINUS;
            case ChubbyLexer.MULTIPLY: return BinaryExpressionNode.Operator.MULTIPLY;
            case ChubbyLexer.DIVIDE: return BinaryExpressionNode.Operator.DIVIDE;
            case ChubbyLexer.MODULO: return BinaryExpressionNode.Operator.MODULO;
            default: return null; // Unknown operator
        }
    }
     // Map ANTLR token type to UnaryExpressionNode.Operator
     private UnaryExpressionNode.Operator mapUnaryOperator(int tokenType) {
        switch (tokenType) {
            case ChubbyLexer.PLUS: return UnaryExpressionNode.Operator.PLUS;
            case ChubbyLexer.MINUS: return UnaryExpressionNode.Operator.MINUS;
            case ChubbyLexer.NOT: return UnaryExpressionNode.Operator.NOT;
            default: return null; // Unknown operator
        }
    }


    @Override public AstNode visitBoolean_expression(ChubbyParser.Boolean_expressionContext ctx) { return visit(ctx.logicalOrExpression()); }
    @Override public AstNode visitExpression(ChubbyParser.ExpressionContext ctx) { return visit(ctx.logicalOrExpression()); }

    @Override public AstNode visitLogicalOrExpression(ChubbyParser.LogicalOrExpressionContext ctx) {
        return visitBinaryExpression(ctx, ChubbyParser.LogicalAndExpressionContext.class, ChubbyLexer.OR);
    }
    @Override public AstNode visitLogicalAndExpression(ChubbyParser.LogicalAndExpressionContext ctx) {
        return visitBinaryExpression(ctx, ChubbyParser.EqualityExpressionContext.class, ChubbyLexer.AND);
    }
    @Override public AstNode visitEqualityExpression(ChubbyParser.EqualityExpressionContext ctx) {
        return visitBinaryExpression(ctx, ChubbyParser.RelationalExpressionContext.class, ChubbyLexer.EQUAL, ChubbyLexer.NOT_EQUAL);
    }
    @Override public AstNode visitRelationalExpression(ChubbyParser.RelationalExpressionContext ctx) {
        return visitBinaryExpression(ctx, ChubbyParser.AdditiveExpressionContext.class, ChubbyLexer.LESS, ChubbyLexer.GREATER, ChubbyLexer.LESS_EQUAL, ChubbyLexer.GREATER_EQUAL);
    }
    @Override public AstNode visitAdditiveExpression(ChubbyParser.AdditiveExpressionContext ctx) {
        return visitBinaryExpression(ctx, ChubbyParser.MultiplicativeExpressionContext.class, ChubbyLexer.PLUS, ChubbyLexer.MINUS);
    }
    @Override public AstNode visitMultiplicativeExpression(ChubbyParser.MultiplicativeExpressionContext ctx) {
        return visitBinaryExpression(ctx, ChubbyParser.UnaryExpressionContext.class, ChubbyLexer.MULTIPLY, ChubbyLexer.DIVIDE, ChubbyLexer.MODULO);
    }

    @Override public AstNode visitUnaryExpression(ChubbyParser.UnaryExpressionContext ctx) {
        if (ctx.postfixExpression() != null) {
            // If it's just a postfix expression, visit that
            return visit(ctx.postfixExpression());
        } else if (ctx.unaryExpression() != null) {
            // If it has an operator prefixing another unary expression
            Token opToken = null;
            if (ctx.PLUS() != null) opToken = ctx.PLUS().getSymbol();
            else if (ctx.MINUS() != null) opToken = ctx.MINUS().getSymbol();
            else if (ctx.NOT() != null) opToken = ctx.NOT().getSymbol();

            if (opToken == null) {
                logError("Missing operator in unary expression", ctx);
                return null;
            }

            UnaryExpressionNode.Operator op = mapUnaryOperator(opToken.getType());
            if (op == null) {
                 logError("Unknown unary operator token type: " + opToken.getType(), ctx);
                 return null;
            }

            AstNode operand = visit(ctx.unaryExpression());
             if (!(operand instanceof ExpressionNode)) {
                 logError("Expected ExpressionNode for unary operand", ctx.unaryExpression());
                 return null;
             }
            return new UnaryExpressionNode(op, (ExpressionNode) operand, line(opToken), col(opToken));
        } else {
             logError("Invalid unary expression structure", ctx);
             return null;
        }
    }

    @Override public AstNode visitPostfixExpression(ChubbyParser.PostfixExpressionContext ctx) {
        // Handles: primaryExpression ( DOT IDENTIFIER | LSQ expression RSQ | LPAREN argument_list? RPAREN )*
        AstNode base = visit(ctx.primaryExpression());
        if (!(base instanceof ExpressionNode)) {
            logError("Expected ExpressionNode for postfix base", ctx.primaryExpression());
            return null;
        }

        ExpressionNode current = (ExpressionNode) base;

        // Iterate through the postfix operations applied to the primary expression
        // The children list includes the primary expression and then pairs/triples for operations
        int i = 1; // Start checking after the primaryExpression
        while (i < ctx.getChildCount()) {
            ParseTree node = ctx.getChild(i);
            if (node instanceof TerminalNode) {
                int symbolType = ((TerminalNode) node).getSymbol().getType();
                Token opToken = ((TerminalNode) node).getSymbol();

                if (symbolType == ChubbyLexer.DOT) {
                    // Member Access: . IDENTIFIER
                    if (i + 1 < ctx.getChildCount() && ctx.getChild(i + 1) instanceof TerminalNode && ((TerminalNode) ctx.getChild(i + 1)).getSymbol().getType() == ChubbyLexer.IDENTIFIER) {
                        TerminalNode idNode = (TerminalNode) ctx.getChild(i + 1);
                        current = new MemberAccessNode(current, idNode.getText(), line(opToken), col(opToken));
                        i += 2; // Consumed DOT and IDENTIFIER
                    } else { logError("Expected IDENTIFIER after DOT in postfix expression", ctx); return current; } // Return partially built expression
                } else if (symbolType == ChubbyLexer.LEFT_SQUARE) {
                    // Array Access: [ expression ]
                    if (i + 2 < ctx.getChildCount() && ctx.getChild(i + 1) instanceof ChubbyParser.ExpressionContext && ctx.getChild(i + 2) instanceof TerminalNode && ((TerminalNode) ctx.getChild(i + 2)).getSymbol().getType() == ChubbyLexer.RIGHT_SQUARE) {
                         AstNode indexExpr = visit(ctx.getChild(i + 1));
                         if (!(indexExpr instanceof ExpressionNode)) { logError("Expected ExpressionNode for array index", (ParserRuleContext)ctx.getChild(i+1)); return current; }
                         current = new ArrayAccessNode(current, (ExpressionNode) indexExpr, line(opToken), col(opToken));
                         i += 3; // Consumed LSQ, expression, RSQ
                    } else { logError("Malformed array access in postfix expression", ctx); return current; }
                } else if (symbolType == ChubbyLexer.LEFT_PAREN) {
                     // Function Call: ( argument_list? )
                     List<ExpressionNode> args = new ArrayList<>();
                     int nextIdx = i + 1;
                     // Check if the next element is an argument list
                     if (nextIdx < ctx.getChildCount() && ctx.getChild(nextIdx) instanceof ChubbyParser.Argument_listContext) {
                         args = visitArgumentList((ChubbyParser.Argument_listContext) ctx.getChild(nextIdx));
                         nextIdx++; // Move past argument list
                     }
                     // Check for the closing parenthesis
                     if (nextIdx < ctx.getChildCount() && ctx.getChild(nextIdx) instanceof TerminalNode && ((TerminalNode) ctx.getChild(nextIdx)).getSymbol().getType() == ChubbyLexer.RIGHT_PAREN) {
                         current = new FunctionCallNode(current, args, line(opToken), col(opToken));
                         i = nextIdx + 1; // Consumed LPAREN, optional args, RPAREN
                     } else { logError("Malformed function call (missing ')') in postfix expression", ctx); return current; }
                } else {
                    // Unexpected token in postfix chain
                    logError("Unexpected token '" + ((TerminalNode)node).getText() + "' in postfix expression", ctx);
                    return current; // Stop processing
                }
            } else {
                 // Should not happen if grammar is correct (expecting operators or primary expr)
                 logError("Unexpected non-terminal node in postfix expression structure", ctx);
                 return current; // Stop processing
            }
        }
        return current;
    }

     // Helper for argument lists
    private List<ExpressionNode> visitArgumentList(ChubbyParser.Argument_listContext ctx) {
        List<ExpressionNode> args = new ArrayList<>();
        if (ctx != null) {
            for (ChubbyParser.ExpressionContext exprCtx : ctx.expression()) {
                AstNode arg = visit(exprCtx);
                if (arg instanceof ExpressionNode) {
                    args.add((ExpressionNode) arg);
                } else {
                    logWarning("Expected ExpressionNode in argument list", exprCtx);
                    // Optionally add a placeholder or skip
                }
            }
        }
        return args;
    }


    @Override public AstNode visitPrimaryExpression(ChubbyParser.PrimaryExpressionContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        } else if (ctx.IDENTIFIER() != null) {
            TerminalNode id = ctx.IDENTIFIER();
            return new IdentifierNode(id.getText(), line(id.getSymbol()), col(id.getSymbol()));
        } else if (ctx.expression() != null) {
            // Parenthesized expression: visit the inner expression
            return visit(ctx.expression());
        } else if (ctx.object_creation() != null) {
            return visit(ctx.object_creation());
        } else if (ctx.THIS() != null) {
            // Represent 'this' as a special IdentifierNode
            // Semantic analysis will validate its usage.
            TerminalNode th = ctx.THIS();
            // Consider creating a dedicated ThisNode if more complex handling is needed
            return new IdentifierNode("this", line(th.getSymbol()), col(th.getSymbol()));
        }
        logError("Unhandled primary expression type", ctx);
        return null;
    }

    @Override
    public AstNode visitObject_creation(ChubbyParser.Object_creationContext ctx) {
        // Grammar: NEW type_specifier ( LPAREN args? RPAREN | ( LSQ expr RSQ )+ )
        TypeNode baseType = (TypeNode) visit(ctx.type_specifier());
        if (baseType == null) {
             logError("Failed to parse type specifier for object creation", ctx.type_specifier());
             return null;
        }

        if (ctx.LEFT_PAREN() != null) {
            // Object creation: new Type(args?)
            List<ExpressionNode> args = visitArgumentList(ctx.argument_list()); // Handles null argument_list
            // ObjectCreationNode expects a non-array type. The baseType here should be non-array.
            if (baseType.isArray()) {
                 logError("Cannot use array type specifier for object creation with parentheses", ctx);
                 return null;
            }
            return new ObjectCreationNode(baseType, args, line(ctx), col(ctx));
        } else if (ctx.LEFT_SQUARE() != null && !ctx.LEFT_SQUARE().isEmpty()) {
            // Array creation: new Type[dim1][dim2]...
            List<ExpressionNode> dimensionSizes = new ArrayList<>();
            // The grammar ensures expression list is not empty if LSQ is present
            for (ChubbyParser.ExpressionContext dimCtx : ctx.expression()) {
                 AstNode dimSize = visit(dimCtx);
                 if (dimSize instanceof ExpressionNode) {
                     dimensionSizes.add((ExpressionNode) dimSize);
                 } else {
                     logError("Expected ExpressionNode for array dimension size", dimCtx);
                     // Handle error: return null or skip dimension? Returning null is safer.
                     return null;
                 }
            }
            // ArrayCreationNode takes the *element* type.
            // The baseType visited from type_specifier is the element type.
            // The number of LSQ/RSQ pairs after type_specifier in the grammar rule
            // `object_creation` defines the dimensions being created.
            // The TypeNode itself should have arrayDimensions=0 here.
             if (baseType.isArray()) {
                 logError("Base type for array creation should not be an array type itself in this context", ctx.type_specifier());
                 // We might allow `new int[][expr]` conceptually, but the AST nodes separate base type and dimensions.
                 // Let's assume baseType is the element type.
                 // Recreate TypeNode with 0 dimensions if needed, though visitType_specifier should handle this.
                 baseType = new TypeNode(baseType.getBaseTypeName(), 0, baseType.getLineNumber(), baseType.getColumnNumber());
             }
            return new ArrayCreationNode(baseType, dimensionSizes, line(ctx), col(ctx));
        } else {
            logError("Invalid object_creation structure (missing parentheses or square brackets)", ctx);
            return null;
        }
    }

    @Override
    public AstNode visitLiteral(ChubbyParser.LiteralContext ctx) {
        Token symbol = ctx.start; // Use start token for position
        int line = line(symbol);
        int col = col(symbol);

        try {
            if (ctx.INTEGER_LITERAL() != null) {
                return new IntegerLiteralNode(Integer.parseInt(ctx.getText()), line, col);
            } else if (ctx.FLOAT_LITERAL() != null) {
                String text = ctx.getText().toUpperCase();
                // Remove F suffix
                text = text.substring(0, text.length() - 1);
                return new FloatLiteralNode(Float.parseFloat(text), line, col);
            } else if (ctx.DOUBLE_LITERAL() != null) {
                String text = ctx.getText().toUpperCase();
                // Remove optional D suffix
                if (text.endsWith("D")) {
                    text = text.substring(0, text.length() - 1);
                }
                return new DoubleLiteralNode(Double.parseDouble(text), line, col);
            } else if (ctx.CHAR_LITERAL() != null) {
                String text = ctx.getText();
                // Remove quotes '...'
                if (text.length() < 2 || !text.startsWith("'") || !text.endsWith("'")) {
                     logError("Invalid char literal format", ctx); return null;
                }
                String inner = text.substring(1, text.length() - 1);
                char value;
                if (inner.length() == 1) {
                    value = inner.charAt(0);
                } else if (inner.length() == 2 && inner.startsWith("\\")) {
                    // Handle basic escapes
                    switch (inner.charAt(1)) {
                        case 'n': value = '\n'; break;
                        case 't': value = '\t'; break;
                        case '\\': value = '\\'; break;
                        case '\'': value = '\''; break;
                        case '"': value = '\"'; break; // Allow escaping double quote in char literal? Maybe not standard.
                        // Add \r, \b, \f if needed
                        default: logError("Unsupported escape sequence in char literal: " + inner, ctx); return null;
                    }
                } else {
                     logError("Invalid char literal content: " + inner, ctx); return null;
                }
                return new CharLiteralNode(value, line, col);
            } else if (ctx.STRING_LITERAL() != null) {
                String text = ctx.getText();
                 // Remove quotes "..."
                if (text.length() < 2 || !text.startsWith("\"") || !text.endsWith("\"")) {
                     logError("Invalid string literal format", ctx); return null;
                }
                String inner = text.substring(1, text.length() - 1);
                // Handle basic escapes - needs a more robust unescaper for production
                String value = inner.replace("\\n", "\n")
                                    .replace("\\t", "\t")
                                    .replace("\\\"", "\"")
                                    .replace("\\\\", "\\");
                // Add other escapes (\r, \b, \f, unicode?)
                return new StringLiteralNode(value, line, col);
            } else if (ctx.TRUE() != null) {
                return new BooleanLiteralNode(true, line, col);
            } else if (ctx.FALSE() != null) {
                return new BooleanLiteralNode(false, line, col);
            } else if (ctx.NULL() != null) {
                return new NullLiteralNode(line, col);
            }
        } catch (NumberFormatException e) {
             logError("Invalid numeric literal format: " + e.getMessage(), ctx);
             return null;
        } catch (Exception e) {
             logError("Error parsing literal: " + e.getMessage(), ctx);
             return null;
        }

        logError("Unknown literal type", ctx);
        return null;
    }

    @Override
    public AstNode visitLvalue(ChubbyParser.LvalueContext ctx) {
        // Grammar: IDENTIFIER | THIS DOT IDENTIFIER | postfixExpression LSQ expression RSQ | postfixExpression DOT IDENTIFIER
        if (ctx.IDENTIFIER() != null && ctx.THIS() == null && ctx.DOT() == null && ctx.postfixExpression() == null) {
            // Simple Identifier case
            TerminalNode id = ctx.IDENTIFIER();
            return new IdentifierNode(id.getText(), line(id.getSymbol()), col(id.getSymbol()));
        } else if (ctx.THIS() != null && ctx.DOT() != null && ctx.IDENTIFIER() != null) {
            // this.member case
            TerminalNode th = ctx.THIS();
            TerminalNode id = ctx.IDENTIFIER();
            // Represent 'this' as a special IdentifierNode or a dedicated ThisNode
            ExpressionNode thisNode = new IdentifierNode("this", line(th.getSymbol()), col(th.getSymbol()));
            return new MemberAccessNode(thisNode, id.getText(), line(ctx), col(ctx));
        } else if (ctx.postfixExpression() != null && ctx.LEFT_SQUARE() != null) {
            // postfixExpression [ expression ] case
            AstNode base = visit(ctx.postfixExpression());
            AstNode index = visit(ctx.expression());
            if (!(base instanceof ExpressionNode)) { logError("Expected ExpressionNode for array base in lvalue", ctx.postfixExpression()); return null; }
            if (!(index instanceof ExpressionNode)) { logError("Expected ExpressionNode for array index in lvalue", ctx.expression()); return null; }
            // Use position of LSQ token
            Token lsqToken = ctx.LEFT_SQUARE().getSymbol();
            return new ArrayAccessNode((ExpressionNode) base, (ExpressionNode) index, line(lsqToken), col(lsqToken));
        } else if (ctx.postfixExpression() != null && ctx.DOT() != null) {
            // postfixExpression . IDENTIFIER case
            AstNode base = visit(ctx.postfixExpression());
            TerminalNode id = ctx.IDENTIFIER();
            if (!(base instanceof ExpressionNode)) { logError("Expected ExpressionNode for object base in lvalue", ctx.postfixExpression()); return null; }
             // Use position of DOT token
            Token dotToken = ctx.DOT().getSymbol();
            return new MemberAccessNode((ExpressionNode) base, id.getText(), line(dotToken), col(dotToken));
        }

        logError("Unhandled lvalue structure", ctx);
        return null;
    }

    @Override
    public AstNode visitChildren(RuleNode node) {
        // This method should ideally not be called if all relevant visit methods are overridden.
        // Log a warning if it's reached unexpectedly.
        System.err.println("Warning: AstBuilderVisitor.visitChildren called for unhandled node: " + node.getClass().getSimpleName() + " (" + node.getText() + ")");
        // Default behavior might visit children, but often doesn't combine results meaningfully for AST building.
        // Returning null is safer to indicate the node wasn't handled properly.
        return null;
    }

    private Visibility getVisibility(ChubbyParser.Visibility_modifierContext ctx) {
        if (ctx == null) {
            // Default visibility (e.g., public or package-private depending on language spec)
            // Assuming public if not specified, adjust if needed.
            // logWarning("Missing visibility modifier, defaulting to PUBLIC", ???); // Need context if called with null
            return Visibility.PUBLIC;
        }
        if (ctx.PUBLIC() != null) return Visibility.PUBLIC;
        if (ctx.PROTECTED() != null) return Visibility.PROTECTED;
        if (ctx.PRIVATE() != null) return Visibility.PRIVATE;
        // Fallback or error? Grammar implies one must be present if modifier exists.
        logWarning("Unknown visibility modifier token, defaulting to PUBLIC", ctx);
        return Visibility.PUBLIC;
    }

    private AssignmentStatementNode.AssignmentOperator getAssignmentOperator(ChubbyParser.Assignment_operatorContext ctx) {
        if (ctx.ASSIGN() != null) return AssignmentStatementNode.AssignmentOperator.ASSIGN;
        if (ctx.PLUS_ASSIGN() != null) return AssignmentStatementNode.AssignmentOperator.PLUS_ASSIGN;
        if (ctx.MINUS_ASSIGN() != null) return AssignmentStatementNode.AssignmentOperator.MINUS_ASSIGN;
        if (ctx.MULTIPLY_ASSIGN() != null) return AssignmentStatementNode.AssignmentOperator.MULTIPLY_ASSIGN;
        if (ctx.DIVIDE_ASSIGN() != null) return AssignmentStatementNode.AssignmentOperator.DIVIDE_ASSIGN;
        if (ctx.MODULO_ASSIGN() != null) return AssignmentStatementNode.AssignmentOperator.MODULUS_ASSIGN;
        // This should not happen if the grammar is correct and covers all cases
        logError("Unknown assignment operator: " + ctx.getText(), ctx);
        throw new IllegalArgumentException("Unknown assignment operator: " + ctx.getText()); // Or return a default/null
    }
}
