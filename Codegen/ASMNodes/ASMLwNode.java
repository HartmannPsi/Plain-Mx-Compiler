package Codegen.ASMNodes;

public class ASMLwNode extends ASMNode {
    public String rd = null, rs1 = null, imm = null;

    public void printToString() {
        printRetract();
        if (rs1 != null)
            System.out.println("lw " + rd + ", " + imm + "(" + rs1 + ")");
        else
            System.out.println("lw " + rd + ", " + imm);
        printNext();
    }
}
