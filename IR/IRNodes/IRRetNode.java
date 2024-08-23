package IR.IRNodes;

public class IRRetNode extends IRNode {
    public String tp = null, val = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.print("ret " + tp);
        if (val != null) {
            System.out.println(" " + val);
        } else {
            System.out.println();
        }
        // shadow = true;

        printNext();
    }

    public String toString() {
        if (val == null) {
            return "ret " + tp;
        } else {
            return "ret " + tp + " " + val;
        }
    }
}
