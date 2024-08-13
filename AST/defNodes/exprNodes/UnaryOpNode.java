package defNodes.exprNodes;

import defNodes.Node;

public class UnaryOpNode extends ExprNode {

    public enum UnaryOprand {
        SAddR, SSubR, SAddL, SSubL, Plus, Minus, LNot, BNot, None
    };

    public Node expr = null;
    public UnaryOprand oprand = null;

    public void printToString() {
        switch (oprand) {
            case SAddL:
                System.out.print("++");
                break;
            case SSubL:
                System.out.print("--");
                break;
            case Plus:
                System.out.print("+");
                break;
            case Minus:
                System.out.print("-");
                break;
            case LNot:
                System.out.print("!");
                break;
            case BNot:
                System.out.print("~");
                break;
            default:
                break;
        }
        expr.printToString();
        switch (oprand) {
            case SAddR:
                System.out.print("++");
                break;
            case SSubR:
                System.out.print("--");
                break;
            default:
                break;
        }
    }
}
