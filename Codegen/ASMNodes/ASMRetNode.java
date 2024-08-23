package Codegen.ASMNodes;

public class ASMRetNode extends ASMNode {
    public void printToString() {
        printRetract();
        System.out.println("ret");
        printNext();
    }
}
