package defNodes.exprNodes;

import defNodes.*;

public class MemAccNode extends ExprNode {
    public Node object = null, member = null;
    // object: expr, member: identifier

    public void printToString() {
        object.printToString();
        System.out.print(".");
        member.printToString();
    }
}
