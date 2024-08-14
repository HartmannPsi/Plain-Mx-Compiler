package defNodes.exprNodes;

public class IdNode extends ExprNode {
    public String id = null;
    public String rename_id = null;

    public void printToString() {
        System.out.print(rename_id);
    }
}
