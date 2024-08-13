package defNodes.exprNodes;

import defNodes.Node;

public class ArrayNode extends ExprNode {
    public Node[] vals = null;

    public void printToString() {
        System.out.print("{");
        for (Node val : vals) {
            val.printToString();
            System.out.print(", ");
        }
        System.out.print("}");
    }
}
