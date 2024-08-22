package Codegen.ASMNodes;

public class ASMRetNode extends ASMNode {
    public void printToString() {
        System.out.println("ret");
        printNext();
    }
}
