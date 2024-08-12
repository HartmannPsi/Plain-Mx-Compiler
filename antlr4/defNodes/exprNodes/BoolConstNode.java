package defNodes.exprNodes;

public class BoolConstNode extends ExprNode {
    public boolean val = false;

    public void printToString() {
        System.out.print(val ? "True" : "False");
    }
}
