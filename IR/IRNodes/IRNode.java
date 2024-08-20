package IR.IRNodes;

public class IRNode {
    public IRNode next = null;
    static public boolean shadow = false;

    public void printToString() {
        printNext();
    }

    public void printNext() {
        if (next != null) {
            next.printToString();
        }
    }
}
