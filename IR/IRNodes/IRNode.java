package IR.IRNodes;

public class IRNode {
    public IRNode next = null;
    static public boolean shadow = false;

    public void printToString() {
        printNext();
    }

    public String toString() {
        return "[ Basic Type ]";
    }

    public IRNode dispose() {

        if (this instanceof IRConstStrNode || this instanceof IRDclFuncNode || this instanceof IRDefClsNode
                || this instanceof IRDefFuncNode || this instanceof IRGlbInitNode || this instanceof IRLabelNode) {
            shadow = false;
            // System.out.println("Fuck");

        }

        if (shadow) {
            if (next == null) {
                return null;
            }

            return next.dispose();
        } else {
            if (next == null) {
                return this;
            }

            if (this instanceof IRDefFuncNode) {
                IRDefFuncNode func_node = (IRDefFuncNode) this;
                func_node.stmt = func_node.stmt.dispose();
                shadow = false;
            }

            if (this instanceof IRBrNode || this instanceof IRRetNode) {
                shadow = true;
                // System.out.println("Fuck you");
            }

            next = next.dispose();
            return this;
        }

    }

    public void printNext() {
        if (next != null) {
            next.printToString();
        }
    }
}
