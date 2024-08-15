package IR.IRNodes;

public class IRNode {
    public IRNode next = null;

    public void printToString() {
        printNext();
    }

    public void printNext() {
        if (next != null) {
            next.printToString();
        }
    }
}
