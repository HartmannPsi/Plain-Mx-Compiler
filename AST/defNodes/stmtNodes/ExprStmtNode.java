package defNodes.stmtNodes;

import defNodes.*;

public class ExprStmtNode extends Node {
    public Node expr = null;

    public void printToString() {
        if (expr != null) {
            expr.printToString();
        }
        System.out.println(";");
    }
}
