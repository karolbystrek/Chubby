package com.karolbystrek.chubbycompiler.compiler;

import com.karolbystrek.chubbycompiler.ast.*;
import com.karolbystrek.chubbycompiler.ast.expression.*;
import com.karolbystrek.chubbycompiler.ast.expression.LiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.*;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.AssignmentStatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.ExpressionStatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.LValueNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.LocalVariableDeclarationNode;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;
import com.karolbystrek.chubbycompiler.symbol.LocalVariableSymbol;
import com.karolbystrek.chubbycompiler.symbol.ParameterSymbol;
import com.karolbystrek.chubbycompiler.symbol.Scope;
import com.karolbystrek.chubbycompiler.symbol.Symbol;
import com.karolbystrek.chubbycompiler.symbol.SymbolKind;

import java.util.List;
import java.util.Objects;

public class SemanticAnalyzer {

    private Scope currentScope;

    public void analyze(ProgramNode programNode) throws CompilationException {
        currentScope = new Scope(null);

        analyzeImports(programNode.getImports());

        analyzeClassDefinitions(programNode.getClassDefinitions());

        // TODO: Perform other top-level semantic checks if needed
    }

    private void analyzeImports(List<ImportStatementNode> imports) {
        // TODO: Implement import resolution and add imported classes/symbols to the global scope
    }

    private void analyzeClassDefinitions(List<ClassDefinitionNode> classDefinitions) throws CompilationException {
        for (ClassDefinitionNode classDef : classDefinitions) {
            analyzeClassDefinition(classDef);
        }
    }

    private void analyzeClassDefinition(ClassDefinitionNode classDef) throws CompilationException {
        Scope classScope = new Scope(currentScope);
        currentScope = classScope;

        // TODO: Add class symbol to parent scope (global scope)
        // TODO: Analyze class members (fields, methods, constructors)

        for (ClassMemberNode member : classDef.getMembers()) {
            if (member instanceof FunctionDefinitionNode function) {
                analyzeFunctionDefinition(function);
            } else if (member instanceof ConstructorDefinitionNode constructor) {
                analyzeConstructorDefinition(constructor);
            }
            // TODO: Add analysis for VariableDefinitionNode (fields)
            // TODO: Add analysis for other ClassMemberNode types
        }

        currentScope = currentScope.getParent();
    }

    private void analyzeConstructorDefinition(ConstructorDefinitionNode constructor) throws CompilationException {
        Scope constructorScope = new Scope(currentScope);
        currentScope = constructorScope;

        // TODO: Add constructor symbol to class scope

        analyzeParameters(constructor.getParameters());

        analyzeStatementList(constructor.getBody());

        currentScope = currentScope.getParent();
    }

    private void analyzeFunctionDefinition(FunctionDefinitionNode function) throws CompilationException {
        Scope functionScope = new Scope(currentScope);
        currentScope = functionScope;

        // TODO: Add function symbol to class scope

        analyzeParameters(function.getParameters());

        analyzeStatementList(function.getBody());

        // TODO: Perform return type checking based on function's returnType and return statements

        currentScope = currentScope.getParent();
    }

    private void analyzeParameters(List<ParameterNode> parameters) throws CompilationException {
        if (parameters == null) {
            return;
        }
        for (ParameterNode parameter : parameters) {
            ParameterSymbol paramSymbol = new ParameterSymbol(
                    parameter.getName(),
                    parameter.getType(),
                    currentScope,
                    parameter.getLineNumber(),
                    parameter.getColumnNumber()
            );
            currentScope.addSymbol(paramSymbol);
            // TODO: Analyze parameter type (e.g., ensure custom types exist)
        }
    }

    private void analyzeStatementList(List<StatementNode> statements) throws CompilationException {
        if (statements == null) {
            return;
        }
        for (StatementNode statement : statements) {
            analyzeStatement(statement);
        }
    }

    private void analyzeStatement(StatementNode statement) throws CompilationException {
        if (statement instanceof LocalVariableDeclarationNode localVariableDeclaration) {
            analyzeLocalVariableDeclaration(localVariableDeclaration);
        } else if (statement instanceof AssignmentStatementNode assignmentStatement) {
            analyzeAssignmentStatement(assignmentStatement);
        } else if (statement instanceof BlockNode blockNode) {
            Scope blockScope = new Scope(currentScope);
            currentScope = blockScope;
            analyzeStatementList(blockNode.getStatements());
            currentScope = currentScope.getParent();
        } else if (statement instanceof ExpressionStatementNode expressionStatementNode) {
            analyzeExpression(expressionStatementNode.getExpression());
            // TODO: Check if the expression has side effects if it's a standalone statement
        }
        // TODO: Add analysis for other statement types (IfStatementNode, WhileStatementNode, ForStatementNode, TryCatchStatementNode, ReturnStatementNode, ThrowStatementNode)
    }

    private void analyzeLocalVariableDeclaration(LocalVariableDeclarationNode node) throws CompilationException {
        LocalVariableSymbol localVariableSymbol = new LocalVariableSymbol(
                node.getVariableName(),
                node.getTypeSpecifier(),
                currentScope,
                node.getLineNumber(),
                node.getColumnNumber()
        );
        // TODO: Add a check here if a symbol with this name already exists in the CURRENT scope before adding.
        currentScope.addSymbol(localVariableSymbol);

        // TODO: Analyze variable type (e.g., ensure custom types exist) - This is a later step

        if (node.getInitializer() != null) {
            analyzeExpression(node.getInitializer());
            TypeNode declaredType = node.getTypeSpecifier();
            TypeNode initializerType = node.getInitializer().getAnalyzedType();

            if (!isAssignable(declaredType, initializerType)) {
                throw new CompilationException(
                        String.format("Cannot initialize variable '%s' of type '%s' with expression of type '%s'",
                                node.getVariableName(),
                                declaredType != null ? declaredType.getBaseTypeName() : "unknown",
                                initializerType != null ? initializerType.getBaseTypeName() : "unknown"),
                        node.getLineNumber(), node.getColumnNumber());
            }
        }
    }

    private void analyzeAssignmentStatement(AssignmentStatementNode node) throws CompilationException {
        analyzeLValue(node.getLValue());

        analyzeExpression(node.getRValue());

        TypeNode lvalueType = getLValueType(node.getLValue());
        TypeNode rvalueType = node.getRValue().getAnalyzedType();

        if (!isAssignable(lvalueType, rvalueType)) {
            throw new CompilationException(
                    String.format("Cannot assign expression of type '%s' to LValue of type '%s'",
                            rvalueType != null ? rvalueType.getBaseTypeName() : "unknown",
                            lvalueType != null ? lvalueType.getBaseTypeName() : "unknown"),
                    node.getLineNumber(), node.getColumnNumber());
        }

        ensureLValueIsAssignable(node.getLValue());
    }

    private void analyzeLValue(LValueNode lValueNode) throws CompilationException {
        if (lValueNode instanceof IdentifierNode identifierNode) {
            Symbol symbol = currentScope.resolve(identifierNode.getName());
            if (symbol == null) {
                throw new CompilationException("Undefined symbol '" + identifierNode.getName() + "'",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }

            if (symbol.getKind() != SymbolKind.LOCAL_VARIABLE && symbol.getKind() != SymbolKind.PARAMETER) {
                // TODO: Add check for FIELD symbol when implemented
                // TODO: Add check for accessibility (e.g., if field is static/instance and context)
                throw new CompilationException("Symbol '" + identifierNode.getName() + "' is not a variable or parameter and cannot be assigned to.",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }

            identifierNode.setSymbol(symbol);

        } else if (lValueNode instanceof MemberAccessNode memberAccessNode) {
            // TODO: Analyze MemberAccessNode as LValue (resolve member, check if field, check accessibility/assignability)
            analyzeExpression(memberAccessNode.getObjectExpression());
        } else if (lValueNode instanceof ArrayAccessNode arrayAccessNode) {
            // TODO: Analyze ArrayAccessNode as LValue (analyze array expression and index expression, check if array element is assignable)
            analyzeExpression(arrayAccessNode.getArrayExpression());
            analyzeExpression(arrayAccessNode.getIndexExpression());
        } else {
            throw new CompilationException("Invalid LValue node type: " + lValueNode.getClass().getSimpleName(),
                    lValueNode.getLineNumber(), lValueNode.getColumnNumber());
        }
    }

    private void analyzeExpression(ExpressionNode expressionNode) throws CompilationException {
        if (expressionNode == null) {
            return;
        }

        // TODO: Implement analysis for other expression types (ObjectCreationNode, ArrayCreationNode, ThisReferenceNode)

        if (expressionNode instanceof IdentifierNode identifierNode) {
            Symbol symbol = currentScope.resolve(identifierNode.getName());
            if (symbol == null) {
                throw new CompilationException("Undefined symbol '" + identifierNode.getName() + "'",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }
            // TODO: Check if the resolved symbol is accessible in this context (e.g., if field is static/instance, visibility)
            // TODO: For now, just ensure it's a variable/parameter/field/method, more checks later
            if (symbol.getKind() != SymbolKind.LOCAL_VARIABLE
                    && symbol.getKind() != SymbolKind.PARAMETER
                    && symbol.getKind() != SymbolKind.FIELD // TODO: Add FIELD symbol kind
                    && symbol.getKind() != SymbolKind.METHOD) { // TODO: Add METHOD symbol kind
                throw new CompilationException("Symbol '" + identifierNode.getName() + "' cannot be used as an expression.",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }

            identifierNode.setSymbol(symbol);
            if (symbol.getKind() == SymbolKind.LOCAL_VARIABLE || symbol.getKind() == SymbolKind.PARAMETER) {
                identifierNode.setAnalyzedType(symbol.getType());
            } else {
                identifierNode.setAnalyzedType(null);
            }

        } else if (expressionNode instanceof IntegerLiteralNode) {
            TypeNode literalType = new TypeNode("int", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof LongLiteralNode) {
            TypeNode literalType = new TypeNode("long", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof FloatLiteralNode) {
            TypeNode literalType = new TypeNode("float", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof DoubleLiteralNode) {
            TypeNode literalType = new TypeNode("double", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof BooleanLiteralNode) {
            TypeNode literalType = new TypeNode("bool", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof CharLiteralNode) {
            TypeNode literalType = new TypeNode("char", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof StringLiteralNode) {
            TypeNode literalType = new TypeNode("string", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        } else if (expressionNode instanceof NullLiteralNode) {
            TypeNode literalType = new TypeNode("null", expressionNode.getLineNumber(), expressionNode.getColumnNumber());
            expressionNode.setAnalyzedType(literalType);
        }
        // TODO: Usunąć ten blok else if po zaimplementowaniu wszystkich podklas LiteralNode
        else if (expressionNode instanceof BinaryExpressionNode binaryExpression) {
            analyzeExpression(binaryExpression.getLeftOperand());
            analyzeExpression(binaryExpression.getRightOperand());
            TypeNode leftType = binaryExpression.getLeftOperand().getAnalyzedType();
            TypeNode rightType = binaryExpression.getRightOperand().getAnalyzedType();
            BinaryExpressionNode.Operator operator = binaryExpression.getOperator();
            TypeNode resultType = determineBinaryExpressionResultType(leftType, operator, rightType, binaryExpression.getLineNumber(), binaryExpression.getColumnNumber());
            binaryExpression.setAnalyzedType(resultType);

        } else if (expressionNode instanceof UnaryExpressionNode unaryExpression) {
            analyzeExpression(unaryExpression.getOperand());
            TypeNode operandType = unaryExpression.getOperand().getAnalyzedType();
            UnaryExpressionNode.Operator operator = unaryExpression.getOperator();
            TypeNode resultType = determineUnaryExpressionResultType(operator, operandType, unaryExpression.getLineNumber(), unaryExpression.getColumnNumber());
            unaryExpression.setAnalyzedType(resultType);
        } else if (expressionNode instanceof MemberAccessNode memberAccessNode) {
            analyzeMemberAccessExpression(memberAccessNode);
        } else if (expressionNode instanceof FunctionCallNode functionCallNode) {
            analyzeFunctionCallExpression(functionCallNode);
        }
        // TODO: Implement analysis for other expression types (ObjectCreationNode, ArrayCreationNode, ThisReferenceNode)
    }

    // TODO: Implement analysis methods for all other AST node types (IfStatementNode, WhileStatementNode, ForStatementNode, TryCatchStatementNode, ReturnStatementNode, ThrowStatementNode)
    // Each method should manage scopes correctly (if applicable) and recursively call analyzeStatement/analyzeExpression.

    // TODO: Implement comprehensive type checking logic (e.g., isAssignable(TypeNode target, TypeNode source), getResultType(BinaryOperator op, TypeNode left, TypeNode right))
    // This logic can be in separate helper methods or even a separate class.

    /**
     * Analyzes a MemberAccessNode to resolve the member (field or method)
     * and determine the expression's type.
     * TODO: Needs comprehensive implementation including static vs instance access, visibility, inheritance, method overloading in resolveMemberSymbol.
     * TODO: This method assumes FieldSymbol and MethodSymbol classes exist and MemberAccessNode has a field to store the resolved Symbol.
     */
    private void analyzeMemberAccessExpression(MemberAccessNode node) throws CompilationException {
        analyzeExpression(node.getObjectExpression());
        TypeNode objectType = node.getObjectExpression().getAnalyzedType();
        String memberName = node.getMemberName();

        if (objectType == null) {
            throw new CompilationException("Cannot access member '" + memberName + "' on an expression with unknown type.",
                    node.getLineNumber(), node.getColumnNumber());
        }
        // TODO: Handle case where objectType is a primitive type (cannot access members on primitives except maybe String if treated specially)

        // Resolve the memberName within the 'objectType' scope.
        Symbol resolvedMember = resolveMemberSymbol(objectType, memberName, node);

        if (resolvedMember == null) {
            throw new CompilationException(String.format("Member '%s' not found in type '%s'.", memberName, objectType.getBaseTypeName()),
                    node.getLineNumber(), node.getColumnNumber());
        }

        // TODO: Check accessibility (public, private, protected) of resolvedMember based on current scope and context.

        // TODO: Store the resolved Symbol in the MemberAccessNode (requires adding a Symbol field to MemberAccessNode)

        if (resolvedMember.getKind() == SymbolKind.FIELD) { // TODO: Check for FIELD symbol kind
            node.setAnalyzedType(resolvedMember.getType());
        } else if (resolvedMember.getKind() == SymbolKind.METHOD) { // TODO: Check for METHOD symbol kind
            node.setAnalyzedType(null);

            // TODO: FunctionCallNode analysis will use this resolved method symbol and check arguments/return type.
        } else {
            throw new CompilationException(String.format("Symbol '%s' in type '%s' is not a field or method and cannot be accessed using dot notation.",
                    memberName, objectType.getBaseTypeName()),
                    node.getLineNumber(), node.getColumnNumber());
        }
    }

    /**
     * Resolves a member symbol (field or method) within the context of a given type.
     * This is where the complex lookup rules, inheritance, and static/instance checks reside.
     * TODO: Implement comprehensive lookup logic.
     * TODO: Needs access to ClassSymbols and their member scopes.
     * TODO: Needs to consider the context (static/instance access) provided by the MemberAccessNode.
     */
    private Symbol resolveMemberSymbol(TypeNode objectType, String memberName, MemberAccessNode node) throws CompilationException {
        if (node.getObjectExpression() instanceof ThisReferenceNode) {
            throw new CompilationException(String.format("Instance member '%s' access via 'this' not fully implemented yet.", memberName), node.getLineNumber(), node.getColumnNumber());
        } else if (node.getObjectExpression() instanceof IdentifierNode idNode && idNode.getSymbol() != null && idNode.getSymbol().getKind() == SymbolKind.CLASS) {
            throw new CompilationException(String.format("Static member '%s' access in class '%s' not fully implemented yet.", memberName, idNode.getName()), node.getLineNumber(), node.getColumnNumber());
        } else {
            throw new CompilationException(String.format("Member '%s' access on arbitrary expression not fully implemented yet.", memberName), node.getLineNumber(), node.getColumnNumber());
        }
    }

    /**
     * Analyzes a FunctionCallNode to resolve the function/method,
     * check argument types, and determine the expression's type.
     * TODO: Needs comprehensive implementation including resolving method symbols, checking arguments against parameters, method overloading, static vs instance calls, visibility.
     * TODO: This method assumes MethodSymbol class exists and FunctionCallNode has a field to store the resolved Symbol.
     */
    private void analyzeFunctionCallExpression(FunctionCallNode node) throws CompilationException {
        analyzeExpression(node.getFunctionExpression());

        ExpressionNode functionExpr = node.getFunctionExpression();
        Symbol functionSymbol = null;

        if (functionExpr instanceof IdentifierNode idNode) {
            Symbol resolved = currentScope.resolve(idNode.getName());
            if (resolved != null && resolved.getKind() == SymbolKind.METHOD) { // TODO: Check for METHOD symbol kind
                functionSymbol = resolved;
            } else {
                throw new CompilationException(String.format("Symbol '%s' is not a function or method and cannot be called.", idNode.getName()),
                        idNode.getLineNumber(), idNode.getColumnNumber());
            }
            // TODO: Implement proper method lookup by name and argument types, considering scope hierarchy and inheritance
        } else if (functionExpr instanceof MemberAccessNode memberAccessNode) {
            throw new CompilationException(String.format("Member '%s' is not a method and cannot be called.", memberAccessNode.getMemberName()),
                    memberAccessNode.getLineNumber(), memberAccessNode.getColumnNumber());
            // TODO: Check static/instance context of the method call based on the objectExpression's analyzed type
        } else {
            throw new CompilationException("Invalid expression used as function or method.",
                    functionExpr.getLineNumber(), functionExpr.getColumnNumber());
        }

        if (functionSymbol == null || functionSymbol.getKind() != SymbolKind.METHOD) {
            throw new CompilationException("Internal error: Failed to resolve valid function/method symbol.",
                    node.getLineNumber(), node.getColumnNumber());
        }

        // TODO: Analyze arguments and get their types
        List<ExpressionNode> arguments = node.getArguments();
        List<TypeNode> argumentTypes = new java.util.ArrayList<>();
        if (arguments != null) {
            for (ExpressionNode arg : arguments) {
                analyzeExpression(arg);
                argumentTypes.add(arg.getAnalyzedType());
            }
        }

        // TODO: Check number and types of arguments against the functionSymbol's parameters.
        // TODO: Implement argument type matching logic (considering assignability).
        // TODO: Handle method overloading if applicable (select the best matching method based on argument types).

        // TODO: Check accessibility (public, private, protected) of resolved method based on current scope and context.

        // TODO: Store the resolved functionSymbol in the FunctionCallNode (requires adding a Symbol field to FunctionCallNode)

        TypeNode returnType = new TypeNode("void", node.getLineNumber(), node.getColumnNumber());
        node.setAnalyzedType(returnType);
    }

    private TypeNode determineBinaryExpressionResultType(TypeNode leftType, BinaryExpressionNode.Operator operator, TypeNode rightType, int lineNumber, int columnNumber) throws CompilationException {
        // TODO: Implement comprehensive type checking rules here!

        if (leftType == null || rightType == null) {
            throw new CompilationException("Operand type is null for binary expression", lineNumber, columnNumber);
        }

        switch (operator) {
            case PLUS, MINUS, MULTIPLY, DIVIDE, MODULO:
                // TODO: Handle different numeric types (int, long, float, double), string concatenation
                if (isNumeric(leftType) && isNumeric(rightType)) {
                    // TODO: Determine the resulting numeric type based on type promotion rules (e.g., int + double -> double)
                    return leftType;
                } else if ("string".equals(leftType.getBaseTypeName()) || "string".equals(rightType.getBaseTypeName())) {
                    // TODO: Implement string concatenation (+ operator)
                    throw new CompilationException("String concatenation not fully implemented yet.", lineNumber, columnNumber);
                } else {
                    throw new CompilationException("Incompatible types for arithmetic operation: " + leftType.getBaseTypeName() + " " + operator + " " + rightType.getBaseTypeName(), lineNumber, columnNumber);
                }
            case EQUAL, NOT_EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL:
                // TODO: Check if types are comparable (e.g., numeric with numeric, boolean with boolean, object reference with null/object reference).
                // TODO: Implement specific comparison rules (e.g., string comparison).
                return new TypeNode("bool", lineNumber, columnNumber);
            case AND, OR:
                if ("bool".equals(leftType.getBaseTypeName()) && "bool".equals(rightType.getBaseTypeName())) {
                    return new TypeNode("bool", lineNumber, columnNumber);
                } else {
                    throw new CompilationException("Incompatible types for logical operation: " + leftType.getBaseTypeName() + " " + operator + " " + rightType.getBaseTypeName(), lineNumber, columnNumber);
                }
            default:
                throw new CompilationException("Unknown binary operator: " + operator, lineNumber, columnNumber);
        }
    }

    private TypeNode determineUnaryExpressionResultType(UnaryExpressionNode.Operator operator, TypeNode operandType, int lineNumber, int columnNumber) throws CompilationException {
        // TODO: Implement comprehensive type checking rules here!

        if (operandType == null) {
            throw new CompilationException("Operand type is null for unary expression", lineNumber, columnNumber);
        }

        switch (operator) {
            case PLUS, MINUS:
                if (isNumeric(operandType)) {
                    return operandType;
                } else {
                    throw new CompilationException("Incompatible type for unary numeric operation: " + operandType.getBaseTypeName(), lineNumber, columnNumber);
                }
            case NOT:
                if ("bool".equals(operandType.getBaseTypeName())) {
                    return new TypeNode("bool", lineNumber, columnNumber);
                } else {
                    throw new CompilationException("Incompatible type for logical NOT operation: " + operandType.getBaseTypeName(), lineNumber, columnNumber);
                }
            default:
                throw new CompilationException("Unknown unary operator: " + operator, lineNumber, columnNumber);
        }
    }

    /**
     * Helper to check if a type is a numeric primitive type.
     * TODO: This needs to be more sophisticated, potentially part of a Type System class.
     */
    private boolean isNumeric(TypeNode type) {
        if (type == null || type.isArray()) {
            return false;
        }
        String baseName = type.getBaseTypeName();
        return "int".equals(baseName) || "long".equals(baseName) || "float".equals(baseName) || "double".equals(baseName) || "byte".equals(baseName) || "char".equals(baseName);
    }

    private boolean isAssignable(TypeNode targetType, TypeNode sourceType) {
        if (targetType == null || sourceType == null) {
            return false;
        }

        boolean baseTypesMatch = targetType.getBaseTypeName().equals(sourceType.getBaseTypeName());
        boolean dimensionsMatch = targetType.getArrayDimensions() == sourceType.getArrayDimensions();

        if (baseTypesMatch && dimensionsMatch) {
            return true;
        }

        // TODO: Add rules for primitive type widening conversions (e.g., int -> long, float -> double, int -> float)
        // TODO: Add rules for String from char[] or vice versa if applicable
        // TODO: Add rules for assigning null to reference types (objects, arrays)
        if ("null".equals(sourceType.getBaseTypeName()) && !targetType.isVoid()) {
            // TODO: Need a way to distinguish primitive types from reference types in TypeNode
            boolean isPrimitive = "byte".equals(targetType.getBaseTypeName()) ||
                    "bool".equals(targetType.getBaseTypeName()) ||
                    "int".equals(targetType.getBaseTypeName()) ||
                    "long".equals(targetType.getBaseTypeName()) ||
                    "float".equals(targetType.getBaseTypeName()) ||
                    "double".equals(targetType.getBaseTypeName()) ||
                    "char".equals(targetType.getBaseTypeName());
            return !isPrimitive;
        }

        // TODO: Add rules for inheritance (source is a subclass of target)
        // TODO: Add array assignment rules (e.g., SubClass[] to SuperClass[])

        return false;
    }

    /**
     * Retrieves the analyzed type of an LValue node.
     * Assumes analyzeLValue has been called on this node.
     * TODO: Implement for MemberAccessNode and ArrayAccessNode.
     */
    /**
     * Retrieves the analyzed type of an LValue node.
     * Assumes analyzeLValue has been called on this node.
     * TODO: Implement for MemberAccessNode and ArrayAccessNode.
     * TODO: This method assumes MemberAccessNode and ArrayAccessNode have resolved Symbols/Types.
     */
    private TypeNode getLValueType(LValueNode lValueNode) throws CompilationException {
        if (lValueNode instanceof IdentifierNode identifierNode) {
            Symbol symbol = identifierNode.getSymbol();
            if (symbol == null) {
                throw new CompilationException("Internal error: Symbol not resolved for LValue identifier '" + identifierNode.getName() + "'",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }
            return symbol.getType();
        }
        // TODO: Implement getting type for MemberAccessNode as LValue
        else if (lValueNode instanceof MemberAccessNode memberAccessNode) {
            throw new CompilationException("Getting type for MemberAccessNode LValue not implemented yet",
                    memberAccessNode.getLineNumber(), memberAccessNode.getColumnNumber());
        }
        // TODO: Implement getting type for ArrayAccessNode as LValue
        else if (lValueNode instanceof ArrayAccessNode arrayAccessNode) {
            throw new CompilationException("Getting type for ArrayAccessNode LValue not implemented yet",
                    arrayAccessNode.getLineNumber(), arrayAccessNode.getColumnNumber());
        }
        else {
            throw new CompilationException("Unsupported LValue node type: " + lValueNode.getClass().getSimpleName(),
                    lValueNode.getLineNumber(), lValueNode.getColumnNumber());
        }
    }

    /**
     * Ensures that the LValue node represents a location that can be assigned to.
     * Assumes analyzeLValue has been called on this node.
     * Checks include whether it's a variable/field (not a literal or function call),
     * whether it's not 'const', and accessibility.
     * TODO: Implement for MemberAccessNode and ArrayAccessNode, and 'const' check.
     * TODO: This method assumes MemberAccessNode and ArrayAccessNode have resolved Symbols/Types.
     */
    private void ensureLValueIsAssignable(LValueNode lValueNode) throws CompilationException {
        if (lValueNode instanceof IdentifierNode identifierNode) {
            Symbol symbol = identifierNode.getSymbol();
            if (symbol == null) {
                throw new CompilationException("Internal error: Symbol not set for IdentifierNode LValue",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }
            // TODO: Check if symbol is declared as 'const' (requires Symbol to store this)
            // TODO: Check accessibility (e.g., cannot assign to a final field outside constructor)
        }
        // TODO: Add checks for assignability for MemberAccessNode (e.g., cannot assign to final field via MemberAccess)
        else if (lValueNode instanceof MemberAccessNode memberAccessNode) {
            throw new CompilationException("Assignability check for MemberAccessNode LValue not implemented yet",
                    memberAccessNode.getLineNumber(), memberAccessNode.getColumnNumber());
        }
        // TODO: Add checks for assignability for ArrayAccessNode (e.g., array element type is assignable)
        else if (lValueNode instanceof ArrayAccessNode arrayAccessNode) {
            throw new CompilationException("Assignability check for ArrayAccessNode LValue not implemented yet",
                    arrayAccessNode.getLineNumber(), arrayAccessNode.getColumnNumber());
        }
        else {
            throw new CompilationException("Internal error: Unsupported LValue node type in assignability check: " + lValueNode.getClass().getSimpleName(),
                    lValueNode.getLineNumber(), lValueNode.getColumnNumber());
        }
    }

    // TODO: Implement analysis methods for all other AST node types (IfStatementNode, WhileStatementNode, ForStatementNode, TryCatchStatementNode, ReturnStatementNode, ThrowStatementNode)
    // Each method should manage scopes correctly (if applicable) and recursively call analyzeStatement/analyzeExpression.

    // TODO: Implement comprehensive type checking logic (e.g., isAssignable(TypeNode target, TypeNode source), getResultType(BinaryOperator op, TypeNode left, TypeNode right))
    // This logic can be in separate helper methods or even a separate class.
}