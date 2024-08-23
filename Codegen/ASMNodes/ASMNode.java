package Codegen.ASMNodes;

public class ASMNode {
    public ASMNode next = null;
    public boolean retract = false;

    public void printNext() {
        if (next != null) {
            next.printToString();
        }
    }

    public void printRetract() {
        if (retract) {
            System.out.print("\t");
        }
    }

    public void printToString() {
        printNext();
    }
}
