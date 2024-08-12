package defNodes.exprNodes;

import defNodes.*;

public class NewExprNode
                extends ExprNode {
        public Node create_type = null;
        public Node init_array = null;
        public Node[] lengths = null;

        public void printToString() {
                System.out.print("new ");
                create_type.printToString();
                if (init_array != null) {
                        System.out.print(" ");
                        init_array.printToString();
                }
        }
}
