package IR.IRNodes;

public class IRBrNode extends IRNode {
    public String cond = null, label_true = null, label_false = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        if (cond != null) {
            System.out.println("br i1 " + cond + ", label %" + label_true + ", label %" + label_false);
        } else {
            System.out.println("br label %" + label_true);
        }
        // shadow = true;

        printNext();
    }

    public String[] use() {
        if (cond != null && isActive(cond)) {
            return new String[] { cond };
        } else {
            return null;
        }
    }

    public String toString() {
        if (cond != null) {
            return "br i1 " + cond + ", label %" + label_true + ", label %" + label_false;
        } else {
            return "br label %" + label_true;
        }
    }
}
