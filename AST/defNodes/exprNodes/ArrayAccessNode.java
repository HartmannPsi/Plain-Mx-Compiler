package defNodes.exprNodes;

import defNodes.Node;

public class ArrayAccessNode extends ExprNode {
    public Node array = null;
    public Node serial = null;

    public void printToString() {
        if (array != null) {
            array.printToString();
        }
        System.out.print("[");
        if (serial != null) {
            serial.printToString();
        }
        System.out.print("]");
    }
}
