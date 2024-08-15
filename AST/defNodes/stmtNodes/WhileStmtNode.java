package defNodes.stmtNodes;

import defNodes.*;

public class WhileStmtNode extends ScopeNode {
    public Node cond = null;
    public Node[] stmts = null;
    public String end_label = null, cond_label = null;

    public void printToString() {
        System.out.print("while (");
        if (cond != null) {
            cond.printToString();
        }
        System.out.println(") {");
        for (Node s : stmts) {
            System.out.print('\t');
            s.printToString();
        }
        System.out.println("}");
    }
}
