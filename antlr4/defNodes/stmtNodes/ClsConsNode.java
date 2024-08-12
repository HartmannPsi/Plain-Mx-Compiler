package defNodes.stmtNodes;

import defNodes.Node;
import defNodes.ScopeNode;

public class ClsConsNode extends ScopeNode {
    public Node id = null;
    public Node[] stmt = null;

    public void printToString() {
        if (id != null) {
            id.printToString();
        }
        System.out.println("() {");
        for (Node s : stmt) {
            System.out.print('\t');
            s.printToString();
        }
        System.out.println("}");
    }
}
