package com.karolbystrek.chubbycompiler.ast.expression;

import java.util.Objects;


public class MemberAccessNode extends ExpressionNode {

    private final ExpressionNode baseExpression;
    private final String memberName;


    public MemberAccessNode(ExpressionNode baseExpression, String memberName, int dotLine, int dotColumn) {
        super(dotLine, dotColumn);
        this.baseExpression = Objects.requireNonNull(baseExpression, "Base expression cannot be null");
        this.memberName = Objects.requireNonNull(memberName, "Member name cannot be null");
    }

    public ExpressionNode getBaseExpression() {
        return baseExpression;
    }

    public String getMemberName() {
        return memberName;
    }

    @Override
    public String toString() {
        return String.format("MemberAccess[base=%s, member='%s' @%d:%d]",
                baseExpression, memberName, getLineNumber(), getColumnNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAccessNode that = (MemberAccessNode) o;
        return baseExpression.equals(that.baseExpression) && memberName.equals(that.memberName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseExpression, memberName);
    }
}