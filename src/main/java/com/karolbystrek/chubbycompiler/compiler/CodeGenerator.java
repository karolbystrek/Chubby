package com.karolbystrek.chubbycompiler.compiler;

import java.util.HashMap;
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
import com.karolbystrek.chubbycompiler.exceptions.CompilationException;

/**
 * Generates JVM bytecode from the Abstract Syntax Tree (AST).
 */
public class CodeGenerator implements Opcodes {

    private static final int JAVA_VERSION = V21;
    private static final String OBJECT_SUPER_CLASS = "java/lang/Object";
    private static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    public CodeGenerator() {}

    /**
     * Generates bytecode for all classes defined in the program AST.
     *
     * @param programNode The root node of the AST.
     * @return A map where keys are fully qualified class names and values are bytecode arrays.
     * @throws CompilationException if errors occur during code generation.
     */
    public Map<String, byte[]> generate(ProgramNode programNode) throws CompilationException {
        Map<String, byte[]> compiledCLasses = new HashMap<>();

        for(ClassDefinitionNode classDefinition : programNode.getClassDefinitions()) {
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

    /**
     * Generates the bytecode for a single class definition
     *
     * @param classDef The ClassDefinitionNode from the AST.
     * @return The bytecode for the class as a byte array.
     */
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
                    generateConstructor(constructor, writer);
                    constructorDefined = true;
                }
                case FunctionDefinitionNode function -> generateFunction(function, writer);
                case VariableDefinitionNode field -> generateField(field, writer);
                default -> throw new CompilationException("Unknown class member type encountered: " + classMember.getClass().getSimpleName());
            }
        }

        if (!constructorDefined) {
            generateDefaultConstructor(OBJECT_SUPER_CLASS, writer);
        }

        writer.visitEnd();
        return writer.toByteArray();
    }

    /**
     * Generates bytecode for a constructor (ConstructorDefinitionNode)
     */
    private void generateConstructor(ConstructorDefinitionNode constructor, ClassWriter writer) {

        StringBuilder descriptorBuilder = new StringBuilder("(");
        for (ParameterNode parameter : constructor.getParameters()) {
            descriptorBuilder.append(mapTypeToDescriptor(parameter.getType()));
        }
        descriptorBuilder.append(")V");
        String descriptor = descriptorBuilder.toString();

        int accessFlags = mapVisibilityToAccessFlags(constructor.getVisibility());

        MethodVisitor visitor = writer.visitMethod(accessFlags, CONSTRUCTOR_METHOD_NAME, descriptor, null, null);
        visitor.visitCode();

        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, OBJECT_SUPER_CLASS, CONSTRUCTOR_METHOD_NAME, "()V", false);

        // TODO: Initialize fields based on constructor parameters or default values
        // TODO: Execute statements in the constructor body

        visitor.visitInsn(RETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    /**
     * Generates bytecode for a default constructor
     */
    private void generateDefaultConstructor(String className, ClassWriter writer) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, CONSTRUCTOR_METHOD_NAME, "()V", null, null);
        visitor.visitCode();
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, className, CONSTRUCTOR_METHOD_NAME, "()V", false);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();
    }

    /**
     * Generates bytecode for a function (FunctionDefinitionNode)
     */
    private void generateFunction(FunctionDefinitionNode function, ClassWriter writer) {
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

        if (mapTypeToDescriptor(function.getReturnType()).equals("V")) {
            visitor.visitInsn(RETURN);
        } else {
            String exceptionType = "java/lang/UnsupportedOperationException";
            visitor.visitTypeInsn(NEW, exceptionType);
            visitor.visitInsn(DUP);
            visitor.visitLdcInsn("Method not implemented yet: " + name);
            visitor.visitMethodInsn(INVOKESPECIAL, exceptionType, CONSTRUCTOR_METHOD_NAME, "(Ljava/lang/String;)V", false);
            visitor.visitInsn(ATHROW);
        }

        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
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
    private String mapTypeToDescriptor(TypeNode typeNode) {
        if (typeNode.isVoid()) {
            return "V";
        }

        String baseDescriptor = switch (typeNode.getBaseTypeName()) {
            case "byte" -> "B";
            case "bool" -> "B";
            case "int" -> "I";
            case "long" -> "J";
            case "float" -> "F";
            case "double" -> "D";
            case "char" -> "C";
            case "string" -> "Ljava/lang/String;";
            // TODO: Handle custom class types correcctly
            // Currently assumes it is in the default package
            default -> "L" + typeNode.getBaseTypeName();
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
            case PUBLIC -> ACC_PUBLIC;
            case PRIVATE -> ACC_PRIVATE;
            case PROTECTED -> ACC_PROTECTED;
            default -> ACC_PRIVATE;
        };
    }
}
