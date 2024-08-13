package defNodes.exprNodes;

import defNodes.*;

public class FStringNode extends ExprNode {
    public String[] literals = null;
    public Node[] exprs = null;

    public void printToString() {
        System.out.print("f\"");
        if (exprs != null) {
            for (int i = 0; i != exprs.length; ++i) {
                System.out.print(literals[i] + "$ ");
                exprs[i].printToString();
                System.out.print(" $");
            }
        }
        System.out.print(literals[literals.length - 1] + "\"");
    }
}
