package com.karolbystrek.chubbycompiler.ast.expression;

import com.karolbystrek.chubbycompiler.ast.statement.simple.LValueNode;

import java.util.Objects;

/**
 * Represents accessing a member (field or method) of an object or class (e.g., obj.member, Class.staticMember).
 * Can function as an LValue if accessing a field.
 */
public class MemberAccessNode extends LValueNode {

    private final ExpressionNode objectExpression;
    private final String memberName;

    public MemberAccessNode(ExpressionNode objectExpression, String memberName, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.objectExpression = Objects.requireNonNull(objectExpression, "Object expression cannot be null");
        this.memberName = Objects.requireNonNull(memberName, "Member name cannot be null");
    }

    public ExpressionNode getObjectExpression() {
        return objectExpression;
    }

    public String getMemberName() {
        return memberName;
    }

    @Override
    public String toString() {
        return "MemberAccessNode{" +
                "object=" + objectExpression +
                ", member='" + memberName + '\'' +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAccessNode that = (MemberAccessNode) o;
        return Objects.equals(objectExpression, that.objectExpression) && Objects.equals(memberName, that.memberName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectExpression, memberName);
    }
}
