package defNodes.exprNodes;

import defNodes.Node;

public class BinaryOpNode extends ExprNode {

    public enum BinaryOprand {
        Mul, Div, Mod, Add, Sub, ShiftL, ShiftR, Lt, Le, Gt, Ge, Eq, Ne, BAnd, BOr, LAnd, LOr, BXor, Assign, None
    };

    public Node lhs = null, rhs = null;
    public BinaryOprand oprand = null;
}
