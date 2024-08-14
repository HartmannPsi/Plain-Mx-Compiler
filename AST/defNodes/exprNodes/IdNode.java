package defNodes.exprNodes;

public class IdNode extends ExprNode {
    public String id = null;
    public String rename_id = null;
    public boolean is_var = true;

    public void printToString() {
        System.out.print((is_var ? "Var" : "Func") + rename_id);
    }
}
