package defNodes.exprNodes;

import defNodes.Node;

public class TernaryOpNode extends ExprNode {

    public Node lhs = null, rhs = null, cond = null;

    public void printToString() {
        cond.printToString();
        System.out.print(" ? ");
        lhs.printToString();
        System.out.print(" : ");
        rhs.printToString();
    }
}
