package Codegen.ASMNodes;

public class ASMCallNode extends ASMNode {
    public String label = null;

    public void printToString() {
        System.out.println("call " + label);
        printNext();
    }
}
