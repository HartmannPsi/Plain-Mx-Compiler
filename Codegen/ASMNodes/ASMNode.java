package Codegen.ASMNodes;

public class ASMNode {
    public ASMNode next = null;

    public void printNext() {
        if (next != null) {
            next.printToString();
        }
    }

    public void printToString() {
        printNext();
    }
}
