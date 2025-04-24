package com.karolbystrek.chubbycompiler.ast.statement.block;

import java.util.List;

import com.karolbystrek.chubbycompiler.ast.statement.StatementNode;

public class TryCatchStatementNode extends StatementNode {

    private final List<StatementNode> tryBody;
    private final List<CatchClauseNode> catchClauses;
    private final FinnallyClauseNode finallyClause;

    public TryCatchStatementNode(List<StatementNode> tryBody,
                                  List<CatchClauseNode> catchClauses,
                                  FinnallyClauseNode finallyClause,
                                  int lineNumber,
                                  int columnNumber) {
        super(lineNumber, columnNumber);
        this.tryBody = tryBody;
        this.catchClauses = catchClauses;
        this.finallyClause = finallyClause;
    }

    public List<StatementNode> getTryBody() {
        return tryBody;
    }

    public List<CatchClauseNode> getCatchClauses() {
        return catchClauses;
    }

    public FinnallyClauseNode getFinallyClause() {
        return finallyClause;
    }

    @Override
    public String toString() {
        return "TryCatchStatementNode{" +
                "tryBody=" + tryBody +
                ", catchClauses=" + catchClauses +
                ", finallyClause=" + finallyClause +
                '}';
    }
}
