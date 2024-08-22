package Codegen.ASMNodes;

public class ASMLuiNode extends ASMNode {
    public String rd = null, imm = null;

    public void printToString() {
        System.out.println("lui " + rd + ", " + imm);
        printNext();
    }
}
