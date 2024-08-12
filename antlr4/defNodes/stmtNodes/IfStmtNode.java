package defNodes.stmtNodes;

import defNodes.*;

public class IfStmtNode extends Node {
    public Node cond = null, then_stmt = null, else_stmt = null;

    public void printToString() {
        System.out.print("if (");
        if (cond != null) {
            cond.printToString();
        }
        System.out.println(") ");

        if (then_stmt != null) {
            then_stmt.printToString();
        }

        if (else_stmt != null) {
            System.out.print(" else ");
            else_stmt.printToString();
        }
    }
}
