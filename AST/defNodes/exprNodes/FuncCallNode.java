package defNodes.exprNodes;

import defNodes.Node;

public class FuncCallNode extends ExprNode {
    public Node id = null;
    public Node[] paras = null;

    public void printToString() {
        if (id != null) {
            id.printToString();
        }
        System.out.print("(");
        if (paras != null) {
            for (Node para : paras) {
                para.printToString();
                System.out.print(", ");
            }
        }
        System.out.print(")");
    }
}
