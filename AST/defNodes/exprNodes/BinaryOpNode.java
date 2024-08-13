package defNodes.exprNodes;

import defNodes.Node;

public class BinaryOpNode extends ExprNode {

    public enum BinaryOprand {
        Mul, Div, Mod, Add, Sub, ShiftL, ShiftR, Lt, Le, Gt, Ge, Eq, Ne, BAnd, BOr, LAnd, LOr, BXor, Assign, None
    };

    public Node lhs = null, rhs = null;
    public BinaryOprand oprand = null;

    public void printToString() {
        lhs.printToString();
        switch (oprand) {
            case Mul:
                System.out.print(" * ");
                break;
            case Div:
                System.out.print(" / ");
                break;
            case Mod:
                System.out.print(" % ");
                break;
            case Add:
                System.out.print(" + ");
                break;
            case Sub:
                System.out.print(" - ");
                break;
            case ShiftL:
                System.out.print(" << ");
                break;
            case ShiftR:
                System.out.print(" >> ");
                break;
            case Lt:
                System.out.print(" < ");
                break;
            case Le:
                System.out.print(" <= ");
                break;
            case Gt:
                System.out.print(" > ");
                break;
            case Ge:
                System.out.print(" >= ");
                break;
            case Eq:
                System.out.print(" == ");
                break;
            case Ne:
                System.out.print(" != ");
                break;
            case BAnd:
                System.out.print(" & ");
                break;
            case BOr:
                System.out.print(" | ");
                break;
            case LAnd:
                System.out.print(" && ");
                break;
            case LOr:
                System.out.print(" || ");
                break;
            case BXor:
                System.out.print(" ^ ");
                break;
            case Assign:
                System.out.print(" = ");
                break;
            case None:
                break;
        }
        rhs.printToString();
    }
}
