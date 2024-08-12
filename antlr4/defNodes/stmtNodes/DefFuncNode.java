package defNodes.stmtNodes;

import defNodes.*;

public class DefFuncNode extends ScopeNode {
    public Node type = null;
    public Node name = null;

    // public class Para {
    // public Node tp;
    // public Node id;
    // };

    // public Para[] paras;
    public Node[] stmt = null;
    public Node[] tps = null;
    public Node[] ids = null;

    public void printToString() {
        if (type != null) {
            type.printToString();
        }
        System.out.print(' ');
        if (name != null) {
            name.printToString();
        }
        System.out.print("(");

        if (tps != null) {
            for (int i = 0; i < tps.length; i++) {
                tps[i].printToString();
                System.out.print(' ');
                ids[i].printToString();
                System.out.print(", ");
            }
        }

        System.out.println(") {");
        for (Node s : stmt) {
            System.out.print('\t');
            s.printToString();
        }
        System.out.println("}");
    }
}
