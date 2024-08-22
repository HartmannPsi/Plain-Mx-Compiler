package Codegen.ASMNodes;

public class ASMLabelNode extends ASMNode {
    public String label = null;

    public void printToString() {
        System.out.println(label + ":");
        printNext();
    }

}
