package defNodes.exprNodes;

public class StringConstNode extends ExprNode {
    public String val = null;

    public void printToString() {
        System.out.print("\"" + val + "\"");
    }
}
