package IR.IRNodes;

public class IRBrNode extends IRNode {
    public String cond = null, label_true = null, label_false = null;

    public void printToString() {
        if (cond != null) {
            System.out.println("br i1 " + cond + ", label %" + label_true + ", label %" + label_false);
        } else {
            System.out.println("br label %" + label_true);
        }

        printNext();
    }
}
