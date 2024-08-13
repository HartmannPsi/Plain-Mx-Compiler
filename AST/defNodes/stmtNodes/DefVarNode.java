package defNodes.stmtNodes;

import defNodes.*;

public class DefVarNode extends Node {
    public Node type = null;

    // public class VarInit {
    // public Node id = null;
    // public Node init = null;

    // public VarInit() {
    // }
    // };

    // public VarInit[] var_init = null;
    public Node[] inits = null;
    public Node[] ids = null;

    public void printToString() {
        type.printToString();
        System.out.print(" ");
        for (int i = 0; i < ids.length; i++) {
            ids[i].printToString();
            if (inits[i] != null) {
                System.out.print(" = ");
                inits[i].printToString();
            }
            System.out.print(", ");
        }
        System.out.println(";");
    }
}
