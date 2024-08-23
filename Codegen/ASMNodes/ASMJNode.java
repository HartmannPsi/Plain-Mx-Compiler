package Codegen.ASMNodes;

public class ASMJNode extends ASMNode {
    public String label = null;

    public void printToString() {
        printRetract();
        System.out.println("j " + label);
        printNext();
    }
}
