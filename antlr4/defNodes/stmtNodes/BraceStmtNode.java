package defNodes.stmtNodes;

import defNodes.*;

public class BraceStmtNode extends ScopeNode {
    public Node[] stmts = null;

    public void printToString() {
        System.out.println("{");
        for (Node stmt : stmts) {
            System.out.print('\t');
            stmt.printToString();
        }
        System.out.println("}");
    }
}
