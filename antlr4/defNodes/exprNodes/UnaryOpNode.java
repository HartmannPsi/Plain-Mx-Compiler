package defNodes.exprNodes;

import defNodes.Node;

public class UnaryOpNode extends ExprNode {

    public enum UnaryOprand {
        SAddR, SSubR, SAddL, SSubL, Plus, Minus, LNot, BNot, None
    };

    public Node expr = null;
    public UnaryOprand oprand = null;
}
