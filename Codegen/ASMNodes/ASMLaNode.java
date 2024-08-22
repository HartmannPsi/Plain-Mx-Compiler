package Codegen.ASMNodes;

public class ASMLaNode extends ASMNode {
    public String rd = null, symbol = null;

    public void printToString() {
        System.out.println("la " + rd + ", " + symbol);
        printNext();
    }
}
