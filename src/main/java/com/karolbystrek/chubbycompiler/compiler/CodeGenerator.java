package com.karolbystrek.chubbycompiler.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.karolbystrek.chubbycompiler.ast.ClassDefinitionNode;
import com.karolbystrek.chubbycompiler.ast.ClassMemberNode;
import com.karolbystrek.chubbycompiler.ast.ConstructorDefinitionNode;
import com.karolbystrek.chubbycompiler.ast.FunctionDefinitionNode;
import com.karolbystrek.chubbycompiler.ast.ParameterNode;
import com.karolbystrek.chubbycompiler.ast.ProgramNode;
import com.karolbystrek.chubbycompiler.ast.TypeNode;
import com.karolbystrek.chubbycompiler.ast.VariableDefinitionNode;
import com.karolbystrek.chubbycompiler.ast.Visibility;
import com.karolbystrek.chubbycompiler.ast.expression.ExpressionNode;
import com.karolbystrek.chubbycompiler.ast.expression.IdentifierNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.BooleanLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.CharLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.DoubleLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.FloatLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.IntegerLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.LongLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.NullLiteralNode;
import com.karolbystrek.chubbycompiler.ast.expression.literal.StringLiteralNode;
import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.AssignmentStatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.ExpressionStatementNode;
import com.karolbystrek.chubbycompiler.ast.statement.simple.LocalVariableDeclarationNode;
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;
import com.karolbystrek.chubbycompiler.symbol.LocalVariableSymbol;
import com.karolbystrek.chubbycompiler.symbol.ParameterSymbol;
import com.karolbystrek.chubbycompiler.symbol.Symbol;
import com.karolbystrek.chubbycompiler.symbol.SymbolKind;

public class CodeGenerator implements Opcodes {

    private static final int JAVA_VERSION = V21;
    private static final String OBJECT_SUPER_CLASS = "java/lang/Object";
    private static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    private Map<Symbol, VariableInfo> localVariableIndexes;
    private int nextLocalVariableIndex;

    public CodeGenerator() {
    }

    public Map<String, byte[]> generate(ProgramNode programNode) throws CompilationException {
        Map<String, byte[]> compiledCLasses = new HashMap<>();

        for (ClassDefinitionNode classDefinition : programNode.getClassDefinitions()) {
            try {
                byte[] bytecode = generateClass(classDefinition);

                compiledCLasses.put(classDefinition.getName(), bytecode);
            } catch (Exception e) {
                throw new CompilationException("Error generating bytecode for class " + classDefinition.getName() + ": " + e.getMessage(), e);
            }
        }

        if (compiledCLasses.isEmpty()) {
            System.err.println("No classes found in the AST to generate.");
        }

        return compiledCLasses;
    }

    private byte[] generateClass(ClassDefinitionNode classDefinition) throws CompilationException {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        String className = classDefinition.getName();

        int accessFlags = mapVisibilityToAccessFlags(classDefinition.getVisibility());
        accessFlags |= ACC_SUPER;

        writer.visit(JAVA_VERSION, accessFlags, className, null, OBJECT_SUPER_CLASS, null);

        // TODO: Visit fields, methods, etc.
        boolean constructorDefined = false;
        for (ClassMemberNode classMember : classDefinition.getMembers()) {
            switch (classMember) {
                case ConstructorDefinitionNode constructor -> {
                    generateConstructor(constructor, writer, className);
                    constructorDefined = true;
                }
                case FunctionDefinitionNode function -> {
                    generateFunction(function, writer, className);
                }
                case VariableDefinitionNode field ->
                    generateField(field, writer);
                default ->
                    throw new CompilationException("Unknown class member type encountered: " + classMember.getClass().getSimpleName());
            }
        }

        if (!constructorDefined) {
            generateDefaultConstructor(OBJECT_SUPER_CLASS, writer);
        }

        writer.visitEnd();
        return writer.toByteArray();
    }

    private void generateConstructor(ConstructorDefinitionNode constructor, ClassWriter writer, String className) throws CompilationException {
        StringBuilder descriptorBuilder = new StringBuilder("(");
        for (ParameterNode parameter : constructor.getParameters()) {
            descriptorBuilder.append(mapTypeToDescriptor(parameter.getType()));
        }
        descriptorBuilder.append(")V");
        String descriptor = descriptorBuilder.toString();

        int accessFlags = mapVisibilityToAccessFlags(constructor.getVisibility());

        MethodVisitor visitor = writer.visitMethod(accessFlags, CONSTRUCTOR_METHOD_NAME, descriptor, null, null);
        visitor.visitCode();

        org.objectweb.asm.Label startMethod = new org.objectweb.asm.Label();
        visitor.visitLabel(startMethod);

        localVariableIndexes = new HashMap<>();
        nextLocalVariableIndex = 0;

        // TODO: Create a proper Symbol for 'this' in SemanticAnalyzer
        Symbol thisSymbol = new LocalVariableSymbol("this", new TypeNode(className, 0, constructor.getLineNumber(), constructor.getColumnNumber()), null, constructor.getLineNumber(), constructor.getColumnNumber());
        localVariableIndexes.put(thisSymbol, new VariableInfo(nextLocalVariableIndex, startMethod));
        nextLocalVariableIndex++;

        for (ParameterNode parameter : constructor.getParameters()) {
            // TODO: Get parameter symbol from the AST node once it's stored there by SemanticAnalyzer
            Symbol parameterSymbol = findSymbolInCurrentScope(parameter.getName(), parameter.getType());

            if (parameterSymbol == null) {
                throw new CompilationException("Internal Error: Parameter symbol not found for '" + parameter.getName() + "' during code generation.",
                        parameter.getLineNumber(), parameter.getColumnNumber());
            }

            int varSize = getJvmTypeSize(parameter.getType());
            localVariableIndexes.put(parameterSymbol, new VariableInfo(nextLocalVariableIndex, startMethod));
            nextLocalVariableIndex += varSize;
        }

        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, OBJECT_SUPER_CLASS, CONSTRUCTOR_METHOD_NAME, "()V", false);

        // TODO: Initialize fields based on constructor parameters or default values AFTER super() call
        generateStatementList(constructor.getBody(), visitor);

        org.objectweb.asm.Label endMethod = new org.objectweb.asm.Label();
        visitor.visitLabel(endMethod);

        visitor.visitInsn(RETURN);

        // TODO: Lepszy sposób identyfikacji symbolu 'this' (np. po rodzaju symbolu)
        for (Map.Entry<Symbol, VariableInfo> entry : localVariableIndexes.entrySet()) {
            Symbol symbol = entry.getKey();
            VariableInfo varInfo = entry.getValue();
            if (symbol.getName().equals("this")) {
                continue;
            }

            visitor.visitLocalVariable(
                    symbol.getName(),
                    mapTypeToDescriptor(symbol.getType()),
                    null,
                    varInfo.startLabel,
                    endMethod,
                    varInfo.index
            );
        }

        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private void generateDefaultConstructor(String className, ClassWriter writer) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, CONSTRUCTOR_METHOD_NAME, "()V", null, null);
        visitor.visitCode();
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, className, CONSTRUCTOR_METHOD_NAME, "()V", false);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();
    }

    private void generateFunction(FunctionDefinitionNode function, ClassWriter writer, String className) throws CompilationException {
        String name = function.getName();
        StringBuilder descriptorBuilder = new StringBuilder("(");
        for (ParameterNode parameter : function.getParameters()) {
            descriptorBuilder.append(mapTypeToDescriptor(parameter.getType()));
        }
        descriptorBuilder.append(")");
        descriptorBuilder.append(mapTypeToDescriptor(function.getReturnType()));
        String descriptor = descriptorBuilder.toString();

        int accessFlags = mapVisibilityToAccessFlags(function.getVisibility());
        if (function.isStatic()) {
            accessFlags |= ACC_STATIC;
        }

        MethodVisitor visitor = writer.visitMethod(accessFlags, name, descriptor, null, null);
        visitor.visitCode();

        org.objectweb.asm.Label startMethod = new org.objectweb.asm.Label();
        visitor.visitLabel(startMethod);

        localVariableIndexes = new HashMap<>();
        nextLocalVariableIndex = 0;

        if (!function.isStatic()) {
            Symbol thisSymbol = new LocalVariableSymbol("this", new TypeNode("L" + className + ";", 0, function.getLineNumber(), function.getColumnNumber()), null, function.getLineNumber(), function.getColumnNumber());
            localVariableIndexes.put(thisSymbol, new VariableInfo(nextLocalVariableIndex, startMethod));
            nextLocalVariableIndex++;
        }

        for (ParameterNode parameter : function.getParameters()) {
            // TODO: Get parameter symbol from the AST node once it's stored there by SemanticAnalyzer
            Symbol parameterSymbol = findSymbolInCurrentScope(parameter.getName(), parameter.getType());

            if (parameterSymbol == null) {
                throw new CompilationException("Internal Error: Parameter symbol not found for '" + parameter.getName() + "' during code generation.",
                        parameter.getLineNumber(), parameter.getColumnNumber());
            }

            int varSize = getJvmTypeSize(parameter.getType());
            localVariableIndexes.put(parameterSymbol, new VariableInfo(nextLocalVariableIndex, startMethod));
            nextLocalVariableIndex += varSize;
        }

        generateStatementList(function.getBody(), visitor);

        org.objectweb.asm.Label endMethod = new org.objectweb.asm.Label();
        visitor.visitLabel(endMethod);

        if (mapTypeToDescriptor(function.getReturnType()).equals("V")) {
            visitor.visitInsn(RETURN);
        } else {
            // TODO: Implement actual return statements (ReturnStatementNode).
            String exceptionType = "java/lang/UnsupportedOperationException";
            visitor.visitTypeInsn(NEW, exceptionType);
            visitor.visitInsn(DUP);
            visitor.visitLdcInsn("Method not implemented yet: " + name + " - Missing return statement?");
            visitor.visitMethodInsn(INVOKESPECIAL, exceptionType, CONSTRUCTOR_METHOD_NAME, "(Ljava/lang/String;)V", false);
            visitor.visitInsn(ATHROW);
        }

        // TODO: Lepszy sposób identyfikacji symbolu 'this' (np. po rodzaju symbolu)
        for (Map.Entry<Symbol, VariableInfo> entry : localVariableIndexes.entrySet()) {
            Symbol symbol = entry.getKey();
            VariableInfo varInfo = entry.getValue();
            if (!function.isStatic() && symbol.getName().equals("this")) {
                continue;
            }

            visitor.visitLocalVariable(
                    symbol.getName(),
                    mapTypeToDescriptor(symbol.getType()),
                    null,
                    varInfo.startLabel,
                    endMethod,
                    varInfo.index
            );
        }

        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private void generateStatementList(List<StatementNode> statements, MethodVisitor visitor) throws CompilationException {
        if (statements == null) {
            return;
        }
        for (StatementNode statement : statements) {
            generateStatement(statement, visitor);
        }
    }

    private void generateStatement(StatementNode statement, MethodVisitor visitor) throws CompilationException {
        switch (statement) {
            case null ->
                throw new CompilationException("Internal Error: Attempted to generate code for a null statement node.", 0, 0);
            case LocalVariableDeclarationNode localVariableDeclaration ->
                generateLocalVariableDeclaration(localVariableDeclaration, visitor);
            case AssignmentStatementNode assignmentStatement ->
                generateAssignmentStatement(assignmentStatement, visitor);
            case BlockNode blockNode ->
                generateStatementList(blockNode.getStatements(), visitor);
            case ExpressionStatementNode expressionStatementNode ->
                generateExpression(expressionStatementNode.getExpression(), visitor);
            // TODO: If the expression leaves a value on the stack (i.e., it's not void), pop it off.
            default ->
                throw new CompilationException("Code generation not implemented for statement type: " + statement.getClass().getSimpleName(),
                        statement.getLineNumber(), statement.getColumnNumber());
        } // TODO: Add generation for other statement types (IfStatementNode, WhileStatementNode, ForStatementNode, TryCatchStatementNode, ReturnStatementNode, ThrowStatementNode)
    }

    /**
     * Generates bytecode for a local variable declaration. Assumes
     * SemanticAnalyzer has resolved the symbol and checked types. TODO: This
     * method contains temporary hacks for symbol lookup and index assignment.
     * TODO: Ensure SemanticAnalyzer stores the Symbol and its index in
     * LocalVariableDeclarationNode.
     */
    private void generateLocalVariableDeclaration(LocalVariableDeclarationNode node, MethodVisitor visitor) throws CompilationException {
        // TODO: Get the Symbol and its assigned index from the AST node once it's stored there by SemanticAnalyzer.
        Symbol variableSymbol = findSymbolInCurrentScope(node.getVariableName(), node.getTypeSpecifier());

        if (variableSymbol == null) {
            throw new CompilationException("Internal Error: Local variable symbol not found for declaration '" + node.getVariableName() + "'",
                    node.getLineNumber(), node.getColumnNumber());
        }

        VariableInfo varInfo = localVariableIndexes.get(variableSymbol);

        if (varInfo == null) {
            int variableIndex = nextLocalVariableIndex;
            org.objectweb.asm.Label startVariable = new org.objectweb.asm.Label();
            varInfo = new VariableInfo(variableIndex, startVariable);
            localVariableIndexes.put(variableSymbol, varInfo);

            int varSize = getJvmTypeSize(node.getTypeSpecifier());
            nextLocalVariableIndex += varSize;
        }

        if (node.getInitializer() != null) {
            generateExpression(node.getInitializer(), visitor);

            int storeOpcode = getStoreOpcode(node.getTypeSpecifier());
            visitor.visitVarInsn(storeOpcode, varInfo.index);
        }

        visitor.visitLabel(varInfo.startLabel);

        // TODO: Add LocalVariableTable attribute using MethodVisitor.visitLocalVariable (Done at the end of the method using the map)
    }

    private void generateAssignmentStatement(AssignmentStatementNode node, MethodVisitor visitor) throws CompilationException {
        generateExpression(node.getRValue(), visitor);

        if (node.getLValue() instanceof IdentifierNode identifierNode) {
            Symbol targetSymbol = identifierNode.getSymbol();

            if (targetSymbol == null) {
                throw new CompilationException("Internal Error: Symbol not resolved for LValue identifier '" + identifierNode.getName() + "' during code generation.",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }

            VariableInfo varInfo = localVariableIndexes.get(targetSymbol);
            if (varInfo == null) {
                throw new CompilationException("Internal Error: VariableInfo not found for symbol '" + identifierNode.getName() + "' during code generation (Index not assigned).",
                        identifierNode.getLineNumber(), identifierNode.getColumnNumber());
            }

            int variableIndex = varInfo.index;

            int storeOpcode = getStoreOpcode(targetSymbol.getType());

            visitor.visitVarInsn(storeOpcode, variableIndex);

        } // TODO: Add generation for MemberAccessNode as LValue (PUTFIELD, PUTSTATIC)
        // TODO: Add generation for ArrayAccessNode as LValue (IASTORE, AASTORE etc.)
        else {
            throw new CompilationException("Code generation not implemented for LValue type in assignment: " + node.getLValue().getClass().getSimpleName(),
                    node.getLValue().getLineNumber(), node.getLValue().getColumnNumber());
        }
    }

    /**
     * Generates bytecode for an expression. The result of the expression should
     * be left on the operand stack. Assumes SemanticAnalyzer has resolved
     * symbols and determined types.
     */
    /**
     * Generates bytecode for an expression. The result of the expression should
     * be left on the operand stack. Assumes SemanticAnalyzer has resolved
     * symbols and determined types.
     */
    private void generateExpression(ExpressionNode expressionNode, MethodVisitor visitor) throws CompilationException {
        switch (expressionNode) {
            case null ->
                throw new CompilationException("Internal Error: Attempted to generate code for a null expression node.", 0, 0);
            case IdentifierNode identifierNode -> {
                Symbol variableSymbol = identifierNode.getSymbol();
                if (variableSymbol == null) {
                    throw new CompilationException("Internal Error: Symbol not resolved for identifier '" + identifierNode.getName() + "' during code generation.",
                            identifierNode.getLineNumber(), identifierNode.getColumnNumber());
                }
                if (variableSymbol.getKind() != SymbolKind.LOCAL_VARIABLE && variableSymbol.getKind() != SymbolKind.PARAMETER) {
                    // TODO: Add generation for FIELD symbols (GETFIELD, GETSTATIC)
                    throw new CompilationException("Code generation not implemented for symbol kind '" + variableSymbol.getKind() + "' used as RValue.",
                            identifierNode.getLineNumber(), identifierNode.getColumnNumber());
                }
                // Zaimplementowany TODO: Znajdź indeks zmiennej lokalnej/parametry przypisany do tego symbolu.
                VariableInfo varInfo = localVariableIndexes.get(variableSymbol);
                if (varInfo == null) {
                    throw new CompilationException("Internal Error: VariableInfo not found for local variable/parameter '" + identifierNode.getName() + "' during code generation (Index not assigned).",
                            identifierNode.getLineNumber(), identifierNode.getColumnNumber());
                }
                int variableIndex = varInfo.index;
                int loadOpcode = getLoadOpcode(variableSymbol.getType());
                visitor.visitVarInsn(loadOpcode, variableIndex);

            }
            case IntegerLiteralNode integerLiteralNode -> {
                int intValue = integerLiteralNode.getValue();
                if (intValue >= -1 && intValue <= 5) {
                    visitor.visitInsn(ICONST_0 + intValue);
                } else if (intValue >= Byte.MIN_VALUE && intValue <= Byte.MAX_VALUE) {
                    visitor.visitIntInsn(BIPUSH, intValue);
                } else if (intValue >= Short.MIN_VALUE && intValue <= Short.MAX_VALUE) {
                    visitor.visitIntInsn(SIPUSH, intValue);
                } else {
                    visitor.visitLdcInsn(intValue);
                }
            }
            case LongLiteralNode longLiteralNode -> {
                long longValue = longLiteralNode.getValue();
                if (longValue == 0L || longValue == 1L) {
                    visitor.visitInsn(LCONST_0 + (int) longValue);
                } else {
                    visitor.visitLdcInsn(longValue);
                }
            }
            case FloatLiteralNode floatLiteralNode -> {
                float floatValue = floatLiteralNode.getValue();
                if (floatValue == 0.0f) {
                    visitor.visitInsn(FCONST_0);
                } else if (floatValue == 1.0f) {
                    visitor.visitInsn(FCONST_1);
                } else if (floatValue == 2.0f) {
                    visitor.visitInsn(FCONST_2);
                } else {
                    visitor.visitLdcInsn(floatValue);
                }
            }
            case DoubleLiteralNode doubleLiteralNode -> {
                double doubleValue = doubleLiteralNode.getValue();
                if (doubleValue == 0.0d) {
                    visitor.visitInsn(DCONST_0);
                } else if (doubleValue == 1.0d) {
                    visitor.visitInsn(DCONST_1);
                } else {
                    visitor.visitLdcInsn(doubleValue);
                }
            }
            case BooleanLiteralNode booleanLiteralNode -> {
                boolean boolValue = booleanLiteralNode.getValue();
                visitor.visitInsn(boolValue ? ICONST_1 : ICONST_0);
            }
            case CharLiteralNode charLiteralNode -> {
                char charValue = charLiteralNode.getValue();
                if (charValue >= -1 && charValue <= 5) {
                    visitor.visitInsn(ICONST_0 + charValue);
                } else if (charValue >= Byte.MIN_VALUE && charValue <= Byte.MAX_VALUE) {
                    visitor.visitIntInsn(BIPUSH, charValue);
                } else if (charValue >= Short.MIN_VALUE && charValue <= Short.MAX_VALUE) {
                    visitor.visitIntInsn(SIPUSH, charValue);
                } else {
                    visitor.visitLdcInsn((int) charValue);
                }
            }
            case StringLiteralNode stringLiteralNode -> {
                String stringValue = stringLiteralNode.getValue();
                visitor.visitLdcInsn(stringValue);
            }
            case NullLiteralNode nullLiteralNode ->
                visitor.visitInsn(ACONST_NULL);
            default ->
                throw new CompilationException("Code generation not implemented for expression type: " + expressionNode.getClass().getSimpleName(),
                        expressionNode.getLineNumber(), expressionNode.getColumnNumber());
        } // TODO: Remove the redundant and incorrect instanceof LiteralNode<?> check
    }

    /**
     * Determines the appropriate JVM store instruction opcode based on the
     * type.
     */
    private int getStoreOpcode(TypeNode type) throws CompilationException {
        // TODO: Handle other types including arrays and objects
        if (type == null || type.isArray()) {
            return ASTORE;
        }
        // TODO: Handle custom object types (also use ASTORE)

        return switch (type.getBaseTypeName()) {
            case "int", "bool", "byte", "char" ->
                ISTORE;
            case "long" ->
                LSTORE;
            case "float" ->
                FSTORE;
            case "double" ->
                DSTORE;
            case "string" ->
                ASTORE;
            // TODO: Handle custom object types (ASTORE)
            default ->
                throw new CompilationException("Code generation not implemented for storing type: " + type.getBaseTypeName());
        };
    }

    /**
     * Determines the appropriate JVM load instruction opcode based on the type.
     */
    private int getLoadOpcode(TypeNode type) throws CompilationException {
        // TODO: Handle other types including arrays and objects
        if (type == null || type.isArray()) {
            return ALOAD;
        }
        // TODO: Handle custom object types (also use ALOAD)

        return switch (type.getBaseTypeName()) {
            case "int", "bool", "byte", "char" ->
                ILOAD;
            case "long" ->
                LLOAD;
            case "float" ->
                FLOAD;
            case "double" ->
                DLOAD;
            case "string" ->
                ALOAD;
            // TODO: Handle custom object types (ALOAD)
            default ->
                throw new CompilationException("Code generation not implemented for loading type: " + type.getBaseTypeName());
        };
    }

    /**
     * Determines the size of a type in JVM local variable slots. Most types
     * take 1 slot, long and double take 2.
     */
    private int getJvmTypeSize(TypeNode type) throws CompilationException {
        if (type == null) {
            throw new CompilationException("Internal Error: Cannot determine JVM size for null type.");
        }
        // TODO: Handle arrays and custom object types (all take 1 slot)
        if (type.isArray() || !isPrimitive(type)) {
            return 1;
        }

        return switch (type.getBaseTypeName()) {
            case "long", "double" ->
                2;
            default ->
                1;
        };
    }

    // TODO: Implement isPrimitive helper method (similar to isNumeric but for all primitives)
    private boolean isPrimitive(TypeNode type) {
        if (type == null || type.isArray()) {
            return false;
        }
        String baseName = type.getBaseTypeName();
        return "byte".equals(baseName) || "bool".equals(baseName) || "int".equals(baseName)
                || "long".equals(baseName) || "float".equals(baseName) || "double".equals(baseName)
                || "char".equals(baseName) || "void".equals(baseName);
    }

    // TODO: Implement findSymbolInCurrentScope or use the resolved symbol from the AST node.
    private Symbol findSymbolInCurrentScope(String name, TypeNode type) {
        return new LocalVariableSymbol(name, type, null, 0, 0);
    }

    /**
     * Generates bytecode for a field (VariableDefinitionNode)
     */
    private void generateField(VariableDefinitionNode field, ClassWriter writer) {
        int accessFlags = mapVisibilityToAccessFlags(field.getVisibility());
        if (field.isStatic()) {
            accessFlags |= ACC_STATIC;
        }
        if (field.isConst()) {
            accessFlags |= ACC_FINAL;
        }

        String name = field.getName();
        String descriptor = mapTypeToDescriptor(field.getType());
        // TODO: Handle constant initial values
        Object initialValue = null;

        FieldVisitor visitor = writer.visitField(accessFlags, name, descriptor, null, initialValue);
        visitor.visitEnd();
    }

    /**
     * Maps a Chubby TypeNode to its JVM descriptor string.
     */
    /**
     * Maps a Chubby TypeNode to its JVM descriptor string.
     */
    /**
     * Maps a Chubby TypeNode to its JVM descriptor string.
     */
    private String mapTypeToDescriptor(TypeNode typeNode) {
        if (typeNode.isVoid()) {
            return "V";
        }

        String baseDescriptor = switch (typeNode.getBaseTypeName()) {
            case "byte" ->
                "B";
            case "bool" ->
                "Z";
            case "int" ->
                "I";
            case "long" ->
                "J";
            case "float" ->
                "F";
            case "double" ->
                "D";
            case "char" ->
                "C";
            case "string" ->
                "Ljava/lang/String;";
            // TODO: Handle custom class types correcctly
            default ->
                "L" + typeNode.getBaseTypeName() + ";";
        };

        StringBuilder descriptor = new StringBuilder();
        for (int i = 0; i < typeNode.getArrayDimensions(); i++) {
            descriptor.append("[");
        }
        descriptor.append(baseDescriptor);

        return descriptor.toString();
    }

    /**
     * Maps the Chubby Visibility enum to ASM access flags.
     *
     * @param visibility The visibility from the AST node.
     * @return The corresponding Opcodes access flag (e.g., ACC_PUBLIC).
     */
    private int mapVisibilityToAccessFlags(Visibility visibility) {
        return switch (visibility) {
            case PUBLIC ->
                ACC_PUBLIC;
            case PRIVATE ->
                ACC_PRIVATE;
            case PROTECTED ->
                ACC_PROTECTED;
            default ->
                ACC_PRIVATE;
        };
    }

    private static class VariableInfo {

        final int index;
        final org.objectweb.asm.Label startLabel;

        VariableInfo(int index, org.objectweb.asm.Label startLabel) {
            this.index = index;
            this.startLabel = startLabel;
        }
    }

    // TODO: Implement getSymbolFromParameterNode by getting the Symbol directly from the ParameterNode AST node.
    private Symbol getSymbolFromParameterNode(ParameterNode node) throws CompilationException {
        return new ParameterSymbol(node.getName(), node.getType(), null, node.getLineNumber(), node.getColumnNumber());
    }

}
