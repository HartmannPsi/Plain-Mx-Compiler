package IR.IRNodes;

public class IRRetNode extends IRNode {
    public String tp = null, val = null;

    public void printToString() {
        System.out.print("ret " + tp);
        if (val != null) {
            System.out.println(" " + val);
        } else {
            System.out.println();
        }
        printNext();
    }
}
