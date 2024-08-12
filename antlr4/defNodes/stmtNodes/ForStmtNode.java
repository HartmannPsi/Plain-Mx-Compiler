package defNodes.stmtNodes;

import defNodes.*;

public class ForStmtNode extends ScopeNode {
    public Node init = null, cond = null, step = null;
    public Node[] stmts = null;

    public void printToString() {
        System.out.print("for (");
        if (init != null) {
            init.printToString();
        }
        // System.out.print("\b");

        if (cond != null) {
            cond.printToString();
        }
        System.out.print("; ");

        if (step != null) {
            step.printToString();
        }

        System.out.println(") {");
        for (Node s : stmts) {
            System.out.print('\t');
            s.printToString();
        }
        System.out.println("}");
    }
}
