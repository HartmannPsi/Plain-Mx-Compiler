package defNodes.stmtNodes;

import defNodes.*;

public class ReturnNode extends Node {
    public Node expr = null;

    public void printToString() {
        System.out.print("return");
        if (expr != null) {
            System.out.print(" ");
            expr.printToString();
        }
        System.out.println(";");
    }
}
