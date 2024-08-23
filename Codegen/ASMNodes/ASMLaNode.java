package Codegen.ASMNodes;

public class ASMLaNode extends ASMNode {
    public String rd = null, label = null;

    public void printToString() {
        printRetract();
        System.out.println("la " + rd + ", " + label);
        printNext();
    }
}
