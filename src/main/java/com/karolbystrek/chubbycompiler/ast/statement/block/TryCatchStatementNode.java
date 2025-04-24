package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.karolbystrek.chubbycompiler.ast.statement.BlockNode;
import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class TryCatchStatementNode extends StatementNode {

    private final BlockNode tryBody;
    private final List<CatchClauseNode> catchClauses;
    private final FinallyClauseNode finallyClause;

    public TryCatchStatementNode(BlockNode tryBody,
                                  List<CatchClauseNode> catchClauses,
                                  FinallyClauseNode finallyClause,
                                  int lineNumber,
                                  int columnNumber) {
        super(lineNumber, columnNumber);
        this.tryBody = Objects.requireNonNull(tryBody, "Try body cannot be null");
        this.catchClauses = catchClauses == null ? Collections.emptyList() : Collections.unmodifiableList(catchClauses);
        if (this.catchClauses.isEmpty() && finallyClause == null) {
            throw new IllegalArgumentException("Try statement must have at least one catch or finally block.");
        }
        this.finallyClause = finallyClause;
    }

    public BlockNode getTryBody() {
        return tryBody;
    }

    public List<CatchClauseNode> getCatchClauses() {
        return catchClauses;
    }

    public FinallyClauseNode getFinallyClause() {
        return finallyClause;
    }

    @Override
    public String toString() {
        return "TryCatchStatementNode{" +
                "tryBody=" + tryBody +
                ", catchClauses=" + catchClauses +
                ", finallyClause=" + finallyClause +
                ", line=" + getLineNumber() +
                ", col=" + getColumnNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TryCatchStatementNode that = (TryCatchStatementNode) o;
        return Objects.equals(tryBody, that.tryBody) && Objects.equals(catchClauses, that.catchClauses) && Objects.equals(finallyClause, that.finallyClause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tryBody, catchClauses, finallyClause);
    }
}
