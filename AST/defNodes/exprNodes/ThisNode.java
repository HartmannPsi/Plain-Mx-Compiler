package defNodes.exprNodes;

public class ThisNode extends ExprNode {

    String rename_id = "%this";

    public void printToString() {
        System.out.print("this");
    }
}
