package Codegen.ASMNodes;

public class ASMLiNode extends ASMNode {
    public String rd = null, imm = null;

    public void printToString() {
        System.out.println("li " + rd + ", " + imm);
        printNext();
    }
}
